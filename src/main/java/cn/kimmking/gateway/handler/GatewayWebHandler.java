package cn.kimmking.gateway.handler;

import cn.kimmking.gateway.GatewayPlugin;
import cn.kimmking.gateway.GatewayPluginChain;
import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
* gateway web handler.
* @Author : kimmking(kimmking@apache.org)
* @create 2024/5/23 下午8:21
*/
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    @Getter
    List<GatewayPlugin> plugins;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        System.out.println("===>>>> KK Gateway web handler ...");

        if(plugins == null || plugins.isEmpty()) {
            String mock = """
                {"result": "no plugin"}
                """;
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory().wrap(mock.getBytes())));
        }

        System.out.println("===>>>> KK Gateway PluginChain for["
                + plugins.stream().map(GatewayPlugin::getName)
                .collect(Collectors.joining(","))+"]");

        return new DefaultGatewayPluginChain(plugins).handle(exchange);
    }

    public static class DefaultGatewayPluginChain implements GatewayPluginChain {

        List<GatewayPlugin> plugins;
         int index = 0;

        public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
            this.plugins = plugins;
        }

        @Override
        public Mono<Void> handle(ServerWebExchange exchange) {
            return Mono.defer(() -> {
                if (index < plugins.size()) {
                    GatewayPlugin plugin = plugins.get(index++);
                    return plugin.handle(exchange, this);
                }
                return Mono.empty();
            });
//            return Flux.fromIterable(plugins)
//                    .filter(plugin -> plugin.support(exchange))
//                    .concatMap(plugin -> plugin.handle(exchange, this)).then();
        }
    }
}
