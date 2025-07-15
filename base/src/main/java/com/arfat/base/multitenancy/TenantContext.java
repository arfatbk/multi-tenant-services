package com.arfat.base.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.arfat.base.multitenancy.TenantConstants.DEFAULT_TENANT_ID;

public final class TenantContext {

    private TenantContext(){}

    private static final Logger log = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> tenantId = new InheritableThreadLocal<>();

    public static void setTenantId(String tenant) {
        log.debug("Setting tenant ID: {}", tenant);
        tenantId.set(tenant);
    }

    public static String getTenantId() {
        return Objects.requireNonNullElse(tenantId.get(), DEFAULT_TENANT_ID);
    }

    public static void clear() {
        log.debug("Clearing tenant ID");
        tenantId.remove();
    }
}
