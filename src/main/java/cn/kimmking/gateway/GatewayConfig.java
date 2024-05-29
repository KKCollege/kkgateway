package cn.kimmking.gateway;

import cn.kimmking.gateway.handler.GatewayWebHandler;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.kk.KkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

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

    public static final String GATEWAY_PREFIX = GatewayPlugin.GATEWAY_PREFIX;

    @Bean
    public RegistryCenter rc() {
        return new KkRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(GATEWAY_PREFIX+"/**", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            System.out.println("kk gateway start");

//            context.getBeansOfType(GatewayPlugin.class)
//                    .forEach((k,v) -> System.out.println(k+":" + v.getName()));
//            context.getBean(GatewayWebHandler.class).getPlugins()
//                    .forEach((k) -> System.out.println(k.getName()));

        };
    }

}
