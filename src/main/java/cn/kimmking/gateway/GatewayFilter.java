package cn.kimmking.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午6:50
 */

@Component
public class GatewayFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println(" ====>>>> gatewat filter ...");

        if(exchange.getRequest().getQueryParams().getFirst("mock")==null) {
            return chain.filter(exchange);
        } else {
            String mock = """
                    {"result":"mock"}
                    """;
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(mock.getBytes())));
        }
    }
}
