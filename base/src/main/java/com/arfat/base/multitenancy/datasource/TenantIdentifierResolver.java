package com.arfat.base.multitenancy.datasource;

import com.arfat.base.multitenancy.TenantContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConditionalOnClass(CurrentTenantIdentifierResolver.class)
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<Object>, HibernatePropertiesCustomizer {

    @Override
    public Object resolveCurrentTenantIdentifier() {
        return TenantContext.getTenantId();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
