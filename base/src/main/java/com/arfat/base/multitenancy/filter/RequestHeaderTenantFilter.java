package com.arfat.base.multitenancy.filter;

import com.arfat.base.multitenancy.TenantConstants;
import com.arfat.base.multitenancy.TenantContext;
import com.arfat.base.multitenancy.datasource.TenantConfigurationProperties;
import io.micrometer.common.KeyValue;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.ServerHttpObservationFilter;

import java.io.IOException;
import java.util.Optional;

public class RequestHeaderTenantFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestHeaderTenantFilter.class);
    private static final String FILTER_APPLIED = RequestDomainTenantFilter.class.getName() + ".APPLIED";
    private final TenantConfigurationProperties tenantConfigurationProperties;

    public RequestHeaderTenantFilter(TenantConfigurationProperties tenantConfigurationProperties) {
        this.tenantConfigurationProperties = tenantConfigurationProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain
            chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        try {
            var tenant = Optional.ofNullable(request.getHeader(TenantConstants.TENANT_ID));

            if (tenant.isEmpty()) {
                this.sendError(response, "Tenant ID header is missing");
                return;
            }

            var optional = tenantConfigurationProperties.getTenants().entrySet().stream()
                    .filter(entry -> entry.getKey().equals(tenant.get()))
                    .findFirst();

            if (optional.isEmpty()) {
                this.sendError(response, "Tenant validation failed for tenant: " + tenant.get());
                return;
            }

            var resolvedTenantId = optional.get().getKey();

            TenantContext.setTenantId(resolvedTenantId);
            MDC.put(TenantConstants.TENANT_ID, resolvedTenantId);
            ServerHttpObservationFilter.findObservationContext(request).ifPresent(context ->
                    context.addHighCardinalityKeyValue(KeyValue.of(TenantConstants.TENANT_ID, resolvedTenantId))
            );
            chain.doFilter(request, response);

        } finally {
            request.removeAttribute(FILTER_APPLIED);
            MDC.clear();
            TenantContext.clear();
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        if (response.isCommitted()) {
            log.trace("Did not write to response since already committed");
            return;
        }

        log.debug("Responding with 403 status code, TenantId not found for in headers: {}", message);
        response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
    }

}