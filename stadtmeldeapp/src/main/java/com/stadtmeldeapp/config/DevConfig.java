package com.stadtmeldeapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@EnableJpaRepositories
@ConfigurationProperties
@Profile("dev")
public class DevConfig {
    
    private String applicationName = "Default - Error";
}

