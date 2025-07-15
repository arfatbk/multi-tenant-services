package com.arfat.api_gateway.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

public class RequestLoggingFilter implements Function<ServerRequest, ServerRequest> {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public ServerRequest apply(ServerRequest serverRequest) {
        log.info("{} {} pathVariables: {} params: {} headers: {} ",
                serverRequest.method(),
                serverRequest.path(),
                serverRequest.pathVariables(),
                serverRequest.params(),
                serverRequest.headers());
        return serverRequest;
    }
}
