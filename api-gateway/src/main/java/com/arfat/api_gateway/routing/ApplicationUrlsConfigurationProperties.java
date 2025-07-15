package com.arfat.api_gateway.routing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@ConfigurationProperties(prefix = "application")
@Getter
@Setter
@Configuration
public class ApplicationUrlsConfigurationProperties {

    private Map<String, String> urls;


    public String getBaseUrlById(String id) {

        return Optional.ofNullable(urls.get(id)).orElseThrow(() ->
                new IllegalArgumentException("URL for " + id + " is not configured in application properties")
        );
    }
}
