package cn.kimmking.gateway.plugin;

import cn.kimmking.gateway.AbstractGatewayPlugin;
import cn.kimmking.gateway.GatewayPlugin;
import cn.kimmking.gateway.GatewayPluginChain;
import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * kkrpc plugin.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午9:53
 */

@Component("kkrpc")
public class KKRpcPlugin extends AbstractGatewayPlugin {

    private static final String NAME = "kkrpc";
    String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer<>();
    List<InstanceMeta> instanceMetas = new ArrayList<>();

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {

        System.out.println(" =======>>>>>>>> [KKRpcPlugin] !!!!!!");

        String service = exchange.getRequest().getPath().value().substring(prefix.length());
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有活着的服务实例
        if(instanceMetas.isEmpty()) {
            instanceMetas = rc.fetchAll(serviceMeta);
            // todo 处理变更
        }

        // 3. 先简化处理，或者第一个实例url

        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);

        // 4. 拿到请求的报文
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        // 5. 通过webclient发送post请求
        WebClient client = WebClient.create(instanceMeta.toUrl());
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        // 6. 通过entity获取响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);
        // body.subscribe(souce -> System.out.println("response:" + souce));
        // 7. 组装响应报文
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("kk.gw.version", "v1.0.0");
        body.flatMap(x->exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));
        return chain.handle(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }
}
