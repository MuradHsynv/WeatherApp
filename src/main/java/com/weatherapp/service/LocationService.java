package com.weatherapp.service;

import com.weatherapp.entity.WeatherData;
import com.weatherapp.repository.WeatherDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationService {

    private final WeatherDataRepository weatherDataRepository;

    public LocationService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public List<WeatherData> getNearbyWeatherData(double latitude, double longitude, double radiusKm, int limit) {
        // Convert kilometers to meters for PostGIS
        double radiusMeters = radiusKm * 1000;
        return weatherDataRepository.findNearbyWeatherData(latitude, longitude, radiusMeters, limit);
    }

    public List<WeatherData> getRecentWeatherData(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return weatherDataRepository.findRecentWeatherData(since);
    }

    public boolean isLocationValid(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
}