package com.arfat.api_gateway.security;

import com.arfat.base.multitenancy.TenantConstants;
import com.arfat.base.multitenancy.TenantContext;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.UUID;
import java.util.function.Function;

public final class RequestHeadersBeforeFilterFunctions {

    private RequestHeadersBeforeFilterFunctions() {
    }

    public static Function<ServerRequest, ServerRequest> enrich() {
        return (request) -> {
            Authentication principal = (Authentication) request.servletRequest().getUserPrincipal();

            String tenantId = TenantContext.getTenantId();
            String requestId = UUID.randomUUID().toString();

            MDC.put(TenantConstants.TENANT_ID, tenantId);
            MDC.put(TenantConstants.REQUEST_ID, requestId);

            if (principal instanceof JwtAuthenticationToken token) {
                String sessionId = token.getTokenAttributes().getOrDefault("sid", "").toString();

                MDC.put(TenantConstants.SESSION_ID, sessionId);

                return ServerRequest.from(request)
                        .header(TenantConstants.TENANT_ID, tenantId)
                        .header(TenantConstants.SESSION_ID, sessionId)
                        .header(TenantConstants.REQUEST_ID, requestId)
                        .build();
            }
            return ServerRequest.from(request)
                    .header(TenantConstants.TENANT_ID, tenantId)
                    .header(TenantConstants.REQUEST_ID, requestId)
                    .build();
        };
    }
}