package com.arfat.base.multitenancy.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.arfat.base.multitenancy.TenantConstants.DEFAULT_TENANT_ID;

@ConditionalOnClass(MultiTenantConnectionProvider.class)
@Component
public class DataSourceConfigurer {

    public static final String DEFAULT_SPRING_DATASOURCE_URL = "spring.datasource.url";
    public static final String DEFAULT_SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    public static final String DEFAULT_SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
    public static final String DEFAULT_SPRING_DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    public static final String DEFAULT_SPRING_DATASOURCE_SCHEMA = "spring.datasource.schema";

    private final Environment environment;
    private final TenantConfigurationProperties tenantConfigurationProperties;
    private final GenericWebApplicationContext applicationContext;

    public DataSourceConfigurer(Environment environment, TenantConfigurationProperties tenantConfigurationProperties, GenericWebApplicationContext applicationContext) {
        this.environment = environment;
        this.tenantConfigurationProperties = tenantConfigurationProperties;
        this.applicationContext = applicationContext;
    }


    @Bean
    @Primary
    DataSource mutiTenantDataSource() {

        var dataSources = tenantDataSources();

        Map<Object, Object> ds = dataSources.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> (Object) e.getKey(),
                        e -> (Object) e.getValue()
                ));
        var multiTenantDataSource = new TenantRoutingDatasource();
        multiTenantDataSource.setTargetDataSources(ds);
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        return multiTenantDataSource;
    }


    @Bean
    DataSource defaultDataSource() {
        String url = environment.getProperty(DEFAULT_SPRING_DATASOURCE_URL);
        String username = environment.getProperty(DEFAULT_SPRING_DATASOURCE_USERNAME);
        String password = environment.getProperty(DEFAULT_SPRING_DATASOURCE_PASSWORD);
        String schema = environment.getProperty(DEFAULT_SPRING_DATASOURCE_SCHEMA);
        String driverClassName = environment.getProperty(DEFAULT_SPRING_DATASOURCE_DRIVER_CLASS_NAME);

        var dataSource = getHikariDataSource(url, password, username, driverClassName);
        dataSource.setSchema(schema);
        return dataSource;
    }

    @Bean
    Map<String, DataSource> tenantDataSources() {
        var dataSources = new HashMap<String, DataSource>();

        dataSources.putIfAbsent(DEFAULT_TENANT_ID, defaultDataSource());

        tenantConfigurationProperties.getTenants()
                .forEach((tenantId, datasourcesProperties) -> {

                    if (applicationContext.getBeanFactory().containsBean(tenantId)) {
                        dataSources.put(tenantId, (DataSource) applicationContext.getBean(tenantId));
                    }
                    dataSources.computeIfAbsent(tenantId, (tId) -> {
                        var datasource = datasourcesProperties.getDatasource();
                        var url = datasource.getUrl();
                        var username = datasource.getUsername();
                        var password = datasource.getPassword();
                        var driverClassName = datasource.getDriverClassName();

                        if (url == null || username == null || password == null || driverClassName == null) {
                            throw new IllegalArgumentException("Datasource properties for tenant " + tenantId + " are not fully defined");
                        }

                        var dataSource = getHikariDataSource(url, password, username, driverClassName);
                        dataSource.setSchema(datasource.getSchema());

                        applicationContext.registerBean(tId, DataSource.class, () -> dataSource);
                        return dataSource;
                    });
                });
        return dataSources;
    }


    private static HikariDataSource getHikariDataSource(String url, String password, String username, String driverClassName) {
        var dsp = new DataSourceProperties();

        dsp.setUrl(url);
        dsp.setPassword(password);
        dsp.setUsername(username);
        dsp.setDriverClassName(driverClassName);
        return dsp.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
