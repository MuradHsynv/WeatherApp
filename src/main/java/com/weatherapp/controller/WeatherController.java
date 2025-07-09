package com.weatherapp.controller;

import com.weatherapp.dto.LocationRequest;
import com.weatherapp.dto.MetricsResponse;
import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.service.MetricsService;
import com.weatherapp.service.WeatherService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;
    private final MetricsService metricsService;

    public WeatherController(WeatherService weatherService, MetricsService metricsService) {
        this.weatherService = weatherService;
        this.metricsService = metricsService;
    }

    @PostMapping("/weather")
    public CompletableFuture<ResponseEntity<WeatherResponse>> getWeather(@Valid @RequestBody LocationRequest locationRequest) {
        logger.info("Received weather request for location: {}, {}",
                locationRequest.getLatitude(), locationRequest.getLongitude());

        return weatherService.getWeatherDataAsync(locationRequest)
                .thenApply(weatherResponse -> {
                    logger.info("Weather data retrieved successfully for ID: {}", weatherResponse.getId());
                    return ResponseEntity.ok(weatherResponse);
                })
                .exceptionally(throwable -> {
                    logger.error("Error retrieving weather data", throwable);
                    return ResponseEntity.internalServerError().build();
                });
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponse> getMetrics() {
        try {
            MetricsResponse metrics = metricsService.getApplicationMetrics();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Error retrieving metrics", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}