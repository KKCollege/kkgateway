package cn.kimmking.gateway.filter;

import cn.kimmking.gateway.GatewayFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Demo filter.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/29 下午9:34
 */
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        System.out.println(" ===>>> filters: demo filter ...");
        exchange.getRequest().getHeaders().toSingleValueMap()
                .forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }
}
