package com.arfat.api_gateway.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.BiFunction;

public class ResponseLoggingFilter implements BiFunction<ServerRequest, ServerResponse, ServerResponse> {

    private static final Logger log = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    public ServerResponse apply(ServerRequest serverRequest, ServerResponse serverResponse) {
        log.info("{} {} pathvariables: {} params: {} headers: {} status: {}",
                serverRequest.method(),
                serverRequest.path(),
                serverRequest.pathVariables(),
                serverRequest.params(),
                serverRequest.headers(),
                serverResponse.statusCode()
        );
        return serverResponse;
    }
}
