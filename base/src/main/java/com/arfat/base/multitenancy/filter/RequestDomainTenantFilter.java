package com.arfat.base.multitenancy.filter;

import com.arfat.base.multitenancy.TenantConstants;
import com.arfat.base.multitenancy.TenantContext;
import com.arfat.base.multitenancy.TenantNotFoundException;
import com.arfat.base.multitenancy.datasource.TenantConfigurationProperties;
import io.micrometer.common.KeyValue;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.ServerHttpObservationFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RequestDomainTenantFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestDomainTenantFilter.class);
    private static final String FILTER_APPLIED = RequestDomainTenantFilter.class.getName() + ".APPLIED";
    private final TenantConfigurationProperties tenantConfigurationProperties;
    private final List<String> DEFAULT_SKIP_PATHS = List.of(
            "/error",
            "/actuator/*"
    );
    private final RequestMatcher matcher = request -> DEFAULT_SKIP_PATHS.stream().noneMatch(request.getServletPath()::startsWith);
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    public RequestDomainTenantFilter(TenantConfigurationProperties tenantConfigurationProperties) {
        this.tenantConfigurationProperties = tenantConfigurationProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain
            chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (!this.matcher.matches(request)) {
            log.debug("Skipping tenant resolution for request: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }
        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        try {

            //TODO: add support for comma separated domain list for single tenant
            // Good to have: Support for tenant identification based on url sub-path ex: domain.com/tenant1, domain.com/tenant2

            var domain = Optional.ofNullable(request.getServerName())
                    .orElseThrow(TenantNotFoundException::new);


            var optional = tenantConfigurationProperties.getTenants().entrySet().stream()
                    .filter(entry -> entry.getValue().getDomain().equals(domain))
                    .findFirst();

            if (optional.isEmpty()) {
                AccessDeniedException exception = new AccessDeniedException("Tenant not found for domain: " + domain);
                this.accessDeniedHandler.handle(request, response, exception);
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

}
