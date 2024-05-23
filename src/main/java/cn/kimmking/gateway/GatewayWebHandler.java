package cn.kimmking.gateway;

import ch.qos.logback.classic.spi.EventArgUtil;
import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * web handler for gateway entry.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/21 下午10:17
 */
public class GatewayWebHandler implements WebHandler {

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer<>();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ====>>>>> KK Gateway WebHandler ...");
//        byte[] bytes = "hello-100".getBytes();
//        return exchange.getResponse()
//                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
//                .doOnNext(data -> exchange.getResponse().getHeaders().setContentLength(data.readableByteCount())));

        // 1. 通过请求路径或者服务名
        System.out.println(exchange.getRequest().getPath().value());
        String service = exchange.getRequest().getPath().value().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有活着的服务实例
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        // 3. 先简化处理，或者第一个实例url

        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        System.out.println(" 2.inst size=" + instanceMetas.size() +  ", inst  " + instanceMeta);
        String url = instanceMeta.toUrl();

        // 4. 拿到请求的报文
        Flux<DataBuffer> reqBody = exchange.getRequest().getBody();

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(reqBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6. 通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);
        //body.subscribe(souce -> System.out.println("response:" + souce));
        // 7. 组装响应报文
        HttpHeaders headers = exchange.getResponse().getHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("kk.gw.version", "v1.0.0");

        return body.flatMap(x -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));

    }
}
