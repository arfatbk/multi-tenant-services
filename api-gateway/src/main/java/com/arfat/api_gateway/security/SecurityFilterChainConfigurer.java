package com.arfat.api_gateway.security;

import com.arfat.base.multitenancy.datasource.TenantConfigurationProperties;
import com.arfat.base.multitenancy.filter.RequestDomainTenantFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
public class SecurityFilterChainConfigurer {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, TenantConfigurationProperties tenantProperties) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth-server/**").permitAll()
                        .requestMatchers("/httpbin/**").permitAll()
                        .anyRequest().authenticated()

                )
                .addFilterBefore(new RequestDomainTenantFilter(tenantProperties), SecurityContextHolderFilter.class)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

}
