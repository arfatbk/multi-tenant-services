package com.arfat.base.multitenancy.datasource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@ConditionalOnClass({DataSource.class, HttpServletRequest.class})
@Configuration
@ConfigurationProperties(prefix = "multitenancy")
@Getter
@Setter
@AllArgsConstructor
public class TenantConfigurationProperties {
    private Map<String, TenantConfiguration> tenants;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TenantConfiguration {
        private DatasourcesProperties datasource;
        private String domain;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class DatasourcesProperties {
        private String url;
        private String username;
        private String password;
        private String schema;
        private String driverClassName;

    }
}


