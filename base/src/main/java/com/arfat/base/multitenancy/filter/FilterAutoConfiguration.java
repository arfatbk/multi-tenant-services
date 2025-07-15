package com.arfat.base.multitenancy.filter;

import com.arfat.base.multitenancy.datasource.TenantConfigurationProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

@Configuration
public class FilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({HttpServletRequest.class})
    LoggingEnrichingFilter loggingEnrichingFilter() {
        return new LoggingEnrichingFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({AccessDeniedException.class, HttpServletRequest.class})
    RequestDomainTenantFilter requestDomainTenantFilter(TenantConfigurationProperties t) {
        return new RequestDomainTenantFilter(t);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({HttpServletRequest.class})
    @ConditionalOnMissingClass("org.springframework.security.access.AccessDeniedException")
    RequestHeaderTenantFilter requestHeaderTenantFilter(TenantConfigurationProperties t) {
        return new RequestHeaderTenantFilter(t);
    }
}

