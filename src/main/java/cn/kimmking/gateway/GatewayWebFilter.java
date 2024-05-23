package cn.kimmking.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * gateway web filter.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午9:22
 */
@Component
public class GatewayWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("===>>> KK Gateway web filter ...");
        if(exchange.getRequest().getQueryParams().getFirst("mock")==null) {
            return chain.filter(exchange);
        }
        String mock = """
                {"result": "mock"}
                """;
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(mock.getBytes())));
    }
}
