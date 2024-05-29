package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午9:50
 */
public interface GatewayPlugin {

     String GATEWAY_PREFIX = "/gw";

     void start();
     void stop();
     String getName();
     boolean support(ServerWebExchange exchange);
//     void enable();
//     void disable();
     Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);
}
