package com.weatherapp.service;

import com.weatherapp.client.OpenWeatherClient;
import com.weatherapp.dto.LocationRequest;
import com.weatherapp.dto.WeatherResponse;
import com.weatherapp.entity.WeatherData;
import com.weatherapp.repository.WeatherDataRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final OpenWeatherClient openWeatherClient;
    private final WeatherDataRepository weatherDataRepository;
    private final GeometryFactory geometryFactory;
    private final Timer weatherApiTimer;
    private final Timer databaseTimer;
    private final Counter requestCounter;

    public WeatherService(OpenWeatherClient openWeatherClient,
                          WeatherDataRepository weatherDataRepository,
                          Timer weatherApiTimer,
                          Timer databaseTimer,
                          MeterRegistry meterRegistry) {
        this.openWeatherClient = openWeatherClient;
        this.weatherDataRepository = weatherDataRepository;
        this.geometryFactory = new GeometryFactory();
        this.weatherApiTimer = weatherApiTimer;
        this.databaseTimer = databaseTimer;
        this.requestCounter = Counter.builder("weather_requests_total")
                .description("Total number of weather requests")
                .register(meterRegistry);
    }

    @Async("weatherTaskExecutor")
    public CompletableFuture<WeatherResponse> getWeatherDataAsync(LocationRequest locationRequest) {
        requestCounter.increment();

        try {
            long startTime = System.currentTimeMillis();

            // Call OpenWeather API
            OpenWeatherClient.OpenWeatherResponse apiResponse = weatherApiTimer.recordCallable(() ->
                    openWeatherClient.getCurrentWeather(locationRequest.getLatitude(), locationRequest.getLongitude())
            );

            long apiResponseTime = System.currentTimeMillis() - startTime;

            if (apiResponse == null) {
                throw new RuntimeException("Failed to get weather data from OpenWeather API");
            }

            // Create WeatherData entity
            WeatherData weatherData = new WeatherData();
            Point location = geometryFactory.createPoint(new Coordinate(locationRequest.getLongitude(), locationRequest.getLatitude()));
            weatherData.setLocation(location);
            weatherData.setCityName(apiResponse.getName());
            weatherData.setCountry(apiResponse.getSys() != null ? apiResponse.getSys().getCountry() : null);
            weatherData.setTemperature(apiResponse.getMain().getTemp());
            weatherData.setFeelsLike(apiResponse.getMain().getFeelsLike());
            weatherData.setHumidity(apiResponse.getMain().getHumidity());
            weatherData.setPressure(apiResponse.getMain().getPressure());

            if (apiResponse.getWeather() != null && !apiResponse.getWeather().isEmpty()) {
                weatherData.setWeatherMain(apiResponse.getWeather().get(0).getMain());
                weatherData.setWeatherDescription(apiResponse.getWeather().get(0).getDescription());
            }

            if (apiResponse.getWind() != null) {
                weatherData.setWindSpeed(apiResponse.getWind().getSpeed());
                weatherData.setWindDirection(apiResponse.getWind().getDeg());
            }

            weatherData.setApiResponseTimeMs(apiResponseTime);

            // Save to database
            WeatherData savedData = databaseTimer.recordCallable(() ->
                    weatherDataRepository.save(weatherData)
            );

            // Convert to response DTO
            WeatherResponse response = convertToResponse(savedData);

            logger.info("Weather data processed successfully for location: {}, {} in {}ms",
                    locationRequest.getLatitude(), locationRequest.getLongitude(), apiResponseTime);

            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            logger.error("Error processing weather data for location: {}, {}",
                    locationRequest.getLatitude(), locationRequest.getLongitude(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private WeatherResponse convertToResponse(WeatherData weatherData) {
        WeatherResponse response = new WeatherResponse();
        response.setId(weatherData.getId());
        response.setLatitude(weatherData.getLocation().getY());
        response.setLongitude(weatherData.getLocation().getX());
        response.setCityName(weatherData.getCityName());
        response.setCountry(weatherData.getCountry());
        response.setTemperature(weatherData.getTemperature());
        response.setFeelsLike(weatherData.getFeelsLike());
        response.setHumidity(weatherData.getHumidity());
        response.setPressure(weatherData.getPressure());
        response.setWeatherMain(weatherData.getWeatherMain());
        response.setWeatherDescription(weatherData.getWeatherDescription());
        response.setWindSpeed(weatherData.getWindSpeed());
        response.setWindDirection(weatherData.getWindDirection());
        response.setTimestamp(weatherData.getCreatedAt());
        response.setApiResponseTimeMs(weatherData.getApiResponseTimeMs());
        return response;
    }
}