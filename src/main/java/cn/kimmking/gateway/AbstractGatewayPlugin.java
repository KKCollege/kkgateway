package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Abstract Gateway Plugin.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/23 下午9:54
 */
public abstract class AbstractGatewayPlugin implements GatewayPlugin {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain) {
        System.out.println(" ====>>>[plugin-" + this.getName() +" support:" + support(exchange) + "]");
        return support(exchange)?doHandle(exchange, chain):chain.handle(exchange);
    }

    public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain);


}
