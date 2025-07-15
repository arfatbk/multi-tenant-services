package com.arfat.base.multitenancy.datasource;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static com.arfat.base.multitenancy.TenantConstants.DEFAULT_TENANT_ID;

@ConditionalOnClass(MultiTenantConnectionProvider.class)
@Component
public class ConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {

    private final TenantRoutingDatasource tenantRoutingDatasource;

    public ConnectionProvider(TenantRoutingDatasource tenantRoutingDatasource) {
        this.tenantRoutingDatasource = tenantRoutingDatasource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection(DEFAULT_TENANT_ID);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        if (tenantIdentifier == null || tenantIdentifier.isEmpty()) {
            throw new IllegalArgumentException("Tenant identifier must not be null or empty");
        }

        return tenantRoutingDatasource.getConnectionFor(tenantIdentifier);
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        connection.setSchema(DEFAULT_TENANT_ID);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }


    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> unwrapType) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}
