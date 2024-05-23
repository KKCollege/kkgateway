package cn.kimmking.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * gateway router.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/21 下午8:32
 */
@Component
public class GatewayRouter {

    @Autowired
    HelloHandler helloHandler;

    @Autowired
    GatewayHandler gatewayHandler;

//    @Autowired
//    GatewayWebHandler gatewayWebHandler;

    @Bean
    public RouterFunction<?> infoRouterFunction1() {
        return route(GET("/info"), request -> ServerResponse.ok().bodyValue("info"));
    }

    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), helloHandler::handle);
    }
//
//    @Bean
//    public RouterFunction<?> gatewayRouterFunction() {
//        return route(GET("/gw").or(POST("/gw/**")), gatewayHandler::handle);
//    }



}
