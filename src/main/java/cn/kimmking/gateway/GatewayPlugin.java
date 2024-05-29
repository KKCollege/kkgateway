package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/29 下午8:12
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    void start();
    void stop();

    String getName();

    boolean support(ServerWebExchange exchange);

    Mono<Void> handle(ServerWebExchange exchange);


}
