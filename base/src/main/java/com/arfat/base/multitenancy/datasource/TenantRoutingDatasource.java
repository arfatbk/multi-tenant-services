package com.arfat.base.multitenancy.datasource;

import com.arfat.base.multitenancy.TenantContext;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;


public class TenantRoutingDatasource extends AbstractRoutingDataSource {

    private static final Logger log = LoggerFactory.getLogger(TenantRoutingDatasource.class);

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenantId();
    }

    @Override
    @NonNull
    protected DataSource determineTargetDataSource() {
        //make sure to fully initialize
        if (this.initialized.compareAndSet(false, true)) {
            this.afterPropertiesSet();
        }
        return super.determineTargetDataSource();
    }

    public Connection getConnectionFor(String tenantIdentifier) throws SQLException {
        var hikariDataSource = (HikariDataSource) this.determineTargetDataSource();
        log.debug("Getting connection for tenant: {}, identifier: {}, jdbcUrl: {}, schema: {}",
                TenantContext.getTenantId(), tenantIdentifier, hikariDataSource.getJdbcUrl(), hikariDataSource.getSchema());
        return hikariDataSource.getConnection();
    }
}
