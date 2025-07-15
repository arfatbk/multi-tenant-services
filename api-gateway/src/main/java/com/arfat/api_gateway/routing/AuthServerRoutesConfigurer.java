package com.arfat.api_gateway.routing;


import com.arfat.api_gateway.logging.RequestLoggingFilter;
import com.arfat.api_gateway.logging.ResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class AuthServerRoutesConfigurer {

    private final static String AUTH_SERVER_CONTEXT_PATH = "auth-server";


    private final ApplicationUrlsConfigurationProperties downstreamUrls;

    public AuthServerRoutesConfigurer(ApplicationUrlsConfigurationProperties downstreamUrls) {
        this.downstreamUrls = downstreamUrls;
    }

    @Bean
    RouterFunction<ServerResponse> authServerRoutes() {

        return route()
                .route(path(String.format("/%s/**", AUTH_SERVER_CONTEXT_PATH)), http())
                    .before(uri(downstreamUrls.getBaseUrlById(AUTH_SERVER_CONTEXT_PATH)))
                .before(rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .before(new RequestLoggingFilter())
                .after(new ResponseLoggingFilter())
                .build();
    }

}
