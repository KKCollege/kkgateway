package cn.kimmking.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
* abstract gateway plugin.
* @Author : kimmking(kimmking@apache.org)
* @create 2024/5/29 下午8:14
*/

public abstract class AbstractGatewayPlugin implements GatewayPlugin{
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        boolean supported = support(exchange);
        System.out.println(" =====>>>> plugin[" + this.getName() + "], support=" + supported);
        return supported ? doHandle(exchange) : Mono.empty();
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract Mono<Void> doHandle(ServerWebExchange exchange);
    public abstract boolean doSupport(ServerWebExchange exchange);
}
