package cn.kimmking.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * hello handler.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/21 下午8:42
 */
@Component
public class HelloHandler {

    Mono<ServerResponse> handle(ServerRequest request) {


        String url = "http://localhost:8081/kkrpc";
        String requestJson = """
                {
                    "service":"cn.kimmking.kkrpc.demo.api.UserService",
                    "methodSign":"findById@1_int",
                    "args":[100]
                }
                """;

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJson).retrieve().toEntity(String.class);

        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.subscribe(souce -> System.out.println("response:" + souce));
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("kk.gw.version", "v1.0.0")
                .body(body, String.class);
    }

}
