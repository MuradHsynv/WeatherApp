package com.weatherapp.dto;

import java.time.LocalDateTime;

public class WeatherResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String cityName;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private Integer humidity;
    private Integer pressure;
    private String weatherMain;
    private String weatherDescription;
    private Double windSpeed;
    private Integer windDirection;
    private LocalDateTime timestamp;
    private Long apiResponseTimeMs;

    // Constructors
    public WeatherResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(Double feelsLike) { this.feelsLike = feelsLike; }

    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }

    public Integer getPressure() { return pressure; }
    public void setPressure(Integer pressure) { this.pressure = pressure; }

    public String getWeatherMain() { return weatherMain; }
    public void setWeatherMain(String weatherMain) { this.weatherMain = weatherMain; }

    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }

    public Integer getWindDirection() { return windDirection; }
    public void setWindDirection(Integer windDirection) { this.windDirection = windDirection; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getApiResponseTimeMs() { return apiResponseTimeMs; }
    public void setApiResponseTimeMs(Long apiResponseTimeMs) { this.apiResponseTimeMs = apiResponseTimeMs; }
}