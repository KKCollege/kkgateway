package cn.kimmking.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * gateway post filter.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午9:29
 */
@Component
public class GatewayPostWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(
                s -> {
                    System.out.println("===>>> post filter");
                    exchange.getAttributes().forEach((k,v) -> System.out.println(k + ":" + v));
                }
        );
    }
}
