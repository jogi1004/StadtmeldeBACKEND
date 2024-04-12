package com.stadtmeldeapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties
@Profile("prod")
public class ProdConfig {
    
    private String applicationName = "Default - Error";
}

