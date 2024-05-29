package cn.kimmking.gateway.web.handler;

import cn.kimmking.gateway.DefaultGatewayPluginChain;
import cn.kimmking.gateway.GatewayFilter;
import cn.kimmking.gateway.GatewayPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
* gateway web handler.
* @Author : kimmking(kimmking@apache.org)
* @create 2024/5/23 下午8:21
*/
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Autowired
    List<GatewayFilter> filters;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println(" ====> KK Gateway web handler ... ");

        if(plugins == null || plugins.isEmpty()) {
            String mock = """
                    {"result":"no plugin"}
                    """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        for(GatewayFilter filter : filters) {
            filter.filter(exchange);
        }

        return new DefaultGatewayPluginChain(plugins).handle(exchange);

//        for(GatewayPlugin plugin : plugins) {
//            if(plugin.support(exchange)) {
//                return plugin.handle(exchange);
//            }
//        }

//        String mock = """
//                    {"result":"no supported plugin"}
//                    """;
//        return exchange.getResponse()
//                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));

    }
}
