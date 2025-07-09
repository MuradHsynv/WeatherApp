package com.weatherapp.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MetricsConfig {

    @Bean
    public Timer weatherApiTimer(MeterRegistry meterRegistry) {
        return Timer.builder("weather_api_requests")
                .description("Timer for weather API requests")
                .register(meterRegistry);
    }

    @Bean
    public Timer databaseTimer(MeterRegistry meterRegistry) {
        return Timer.builder("database_operations")
                .description("Timer for database operations")
                .register(meterRegistry);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}