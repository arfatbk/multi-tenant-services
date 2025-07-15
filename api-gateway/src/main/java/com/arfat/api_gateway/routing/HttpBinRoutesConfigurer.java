package com.arfat.api_gateway.routing;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class HttpBinRoutesConfigurer {

    @Bean
    RouterFunction<ServerResponse> httpBinRoutes() {
        return route()
                .route(path("/httpbin/**"), http())
                .before(uri("https://httpbin.org/"))
                .before(rewritePath("/api/httpbin/(?<segment>.*)", "/${segment}"))
                .build();
    }

}
