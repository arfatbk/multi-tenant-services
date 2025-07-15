package com.arfat.api_gateway.routing;

import com.arfat.api_gateway.logging.RequestLoggingFilter;
import com.arfat.api_gateway.logging.ResponseLoggingFilter;
import com.arfat.api_gateway.security.RequestHeadersBeforeFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class UserServiceRoutesConfigurer {

    private final static String USER_SERVICE_CONTEXT_PATH = "user-service";

    private final ApplicationUrlsConfigurationProperties downstreamUrls;

    public UserServiceRoutesConfigurer(ApplicationUrlsConfigurationProperties downstreamUrls) {
        this.downstreamUrls = downstreamUrls;
    }

    @Bean
    RouterFunction<ServerResponse> userServiceRoutes() {

        return route()
                .route(path(String.format("/%s/**", USER_SERVICE_CONTEXT_PATH)), http())
                .before(uri(downstreamUrls.getBaseUrlById(USER_SERVICE_CONTEXT_PATH)))
                .before(rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .before(RequestHeadersBeforeFilterFunctions.enrich())
                .before(new RequestLoggingFilter())
                .after(new ResponseLoggingFilter())
                .filter(tokenRelay())
                .build();
    }


}

