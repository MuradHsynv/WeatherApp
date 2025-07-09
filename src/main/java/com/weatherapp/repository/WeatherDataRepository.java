package com.weatherapp.repository;

import com.weatherapp.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    @Query("SELECT w FROM WeatherData w WHERE w.createdAt >= :since ORDER BY w.createdAt DESC")
    List<WeatherData> findRecentWeatherData(@Param("since") LocalDateTime since);

    @Query(value = "SELECT * FROM weather_data WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radiusMeters) ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<WeatherData> findNearbyWeatherData(@Param("latitude") double latitude,
                                            @Param("longitude") double longitude,
                                            @Param("radiusMeters") double radiusMeters,
                                            @Param("limit") int limit);

    @Query("SELECT AVG(w.apiResponseTimeMs) FROM WeatherData w WHERE w.createdAt >= :since")
    Double getAverageResponseTime(@Param("since") LocalDateTime since);
}
