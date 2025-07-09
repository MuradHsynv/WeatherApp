package com.weatherapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.weatherapp.repository")
public class DatabaseConfig {
    // Additional database configuration if needed
}