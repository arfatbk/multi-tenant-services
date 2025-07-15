package com.arfat.base.multitenancy.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Optional;

public class LoggingEnrichingFilter implements Filter {

    private static final String FILTER_APPLIED = LoggingEnrichingFilter.class.getName() + ".APPLIED";


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
            var sessionId = Optional.ofNullable(request.getHeader("sessionId"))
                    .orElse("UNKNOWN");

            var requestId = Optional.ofNullable(request.getHeader("requestId"))
                    .orElse("UNKNOWN");

            MDC.put("sessionId", sessionId);
            MDC.put("requestId", requestId);

            chain.doFilter(request, response);

        } finally {
            request.removeAttribute(FILTER_APPLIED);
            MDC.clear();
        }
    }

}
