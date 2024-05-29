package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午10:03
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
