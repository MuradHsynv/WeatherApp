package com.weatherapp.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class OpenWeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public OpenWeatherClient(@Value("${openweather.api.base-url}") String baseUrl,
                             @Value("${openweather.api.key}") String apiKey,
                             RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    public OpenWeatherResponse getCurrentWeather(double latitude, double longitude) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/weather")
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .build()
                    .toString();

            logger.debug("Calling OpenWeather API: {}", url);

            return restTemplate.getForObject(url, OpenWeatherResponse.class);
        } catch (Exception error) {
            logger.error("Error calling OpenWeather API", error);
            throw new RuntimeException("Failed to fetch weather data", error);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenWeatherResponse {
        @JsonProperty("coord")
        private Coord coord;

        @JsonProperty("weather")
        private List<Weather> weather;

        @JsonProperty("main")
        private Main main;

        @JsonProperty("wind")
        private Wind wind;

        @JsonProperty("name")
        private String name;

        @JsonProperty("sys")
        private Sys sys;

        // Getters and Setters
        public Coord getCoord() { return coord; }
        public void setCoord(Coord coord) { this.coord = coord; }

        public List<Weather> getWeather() { return weather; }
        public void setWeather(List<Weather> weather) { this.weather = weather; }

        public Main getMain() { return main; }
        public void setMain(Main main) { this.main = main; }

        public Wind getWind() { return wind; }
        public void setWind(Wind wind) { this.wind = wind; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Sys getSys() { return sys; }
        public void setSys(Sys sys) { this.sys = sys; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        @JsonProperty("lon")
        private Double lon;

        @JsonProperty("lat")
        private Double lat;

        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @JsonProperty("main")
        private String main;

        @JsonProperty("description")
        private String description;

        public String getMain() { return main; }
        public void setMain(String main) { this.main = main; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private Double temp;

        @JsonProperty("feels_like")
        private Double feelsLike;

        @JsonProperty("humidity")
        private Integer humidity;

        @JsonProperty("pressure")
        private Integer pressure;

        public Double getTemp() { return temp; }
        public void setTemp(Double temp) { this.temp = temp; }

        public Double getFeelsLike() { return feelsLike; }
        public void setFeelsLike(Double feelsLike) { this.feelsLike = feelsLike; }

        public Integer getHumidity() { return humidity; }
        public void setHumidity(Integer humidity) { this.humidity = humidity; }

        public Integer getPressure() { return pressure; }
        public void setPressure(Integer pressure) { this.pressure = pressure; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        @JsonProperty("speed")
        private Double speed;

        @JsonProperty("deg")
        private Integer deg;

        public Double getSpeed() { return speed; }
        public void setSpeed(Double speed) { this.speed = speed; }

        public Integer getDeg() { return deg; }
        public void setDeg(Integer deg) { this.deg = deg; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        @JsonProperty("country")
        private String country;

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }
}