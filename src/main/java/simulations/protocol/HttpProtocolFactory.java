package simulations.protocol;

import io.gatling.javaapi.http.HttpProtocolBuilder;
import static io.gatling.javaapi.http.HttpDsl.http;
import simulations.config.GatlingConfig;

public class HttpProtocolFactory {

    public static HttpProtocolBuilder createHttpProtocol() {
        return http
                .baseUrl(GatlingConfig.BASE_URL.getValue())
                .acceptHeader("application/json")
                .contentTypeHeader("application/json")
                .userAgentHeader("Gatling-LoadTest/1.0")
                .disableCaching();
    }
}