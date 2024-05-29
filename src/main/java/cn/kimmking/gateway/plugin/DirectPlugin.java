package cn.kimmking.gateway.plugin;

import cn.kimmking.gateway.AbstractGatewayPlugin;
import cn.kimmking.gateway.GatewayPluginChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Direct plugin.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/24 下午11:44
 */
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {

    private static final String NAME = "direct";

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {

        System.out.println(" =======>>>>>>>> [DirectPlugin] !!!!!!");

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        String backend = queryParams.getFirst("backend");
        String method = exchange.getRequest().getMethod().name();
        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
        responseHeaders.add("Content-Type", "application/json");
        responseHeaders.add("kk.gw.version", "v1.0.0");
        responseHeaders.add("kk.gw.method", method);

        if(backend == null || backend.isEmpty()) {
            return requestBody.flatMap(x -> exchange.getResponse().writeWith(Mono.just(x)))
                    .then(chain.handle(exchange));
        }

        WebClient client = WebClient.create(backend);
        Mono<ResponseEntity<String>> entity = null;
        if(method.equals("POST")) {
            entity = client.get()
                    .header("Content-Type", "application/json")
                    .retrieve().toEntity(String.class);

        } else {
            entity = client.post()
                    .header("Content-Type", "application/json")
                    .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        }
        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.flatMap(x->exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));
        return chain.handle(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(GATEWAY_PREFIX + "/" + getName()+ "/");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
