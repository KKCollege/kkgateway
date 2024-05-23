package cn.kimmking.gateway;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.kk.KkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * gateway config.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/21 下午9:09
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        return new KkRegistryCenter();
    }

//    @Bean("dispatcherHandler")
//    public DispatcherHandler dispatcherHandler() {
//        return new DispatcherHandler();
//    }

    @Bean("gatewayWebHandler")
    GatewayWebHandler gatewayWebHandler() {
        return new GatewayWebHandler();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            Properties mappings = new Properties();
            mappings.put("/ga/**", "gatewayWebHandler");
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("gateway start");
        };
    }
}
