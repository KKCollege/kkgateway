package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway plugin chain.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/29 下午9:04
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
