package cn.kimmking.gateway;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.kk.KkRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
