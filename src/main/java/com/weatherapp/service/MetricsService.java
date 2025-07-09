package com.weatherapp.service;

import com.weatherapp.dto.MetricsResponse;
import com.weatherapp.repository.WeatherDataRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;

@Service
public class MetricsService {

    private final WeatherDataRepository weatherDataRepository;
    private final MeterRegistry meterRegistry;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;

    public MetricsService(WeatherDataRepository weatherDataRepository, MeterRegistry meterRegistry) {
        this.weatherDataRepository = weatherDataRepository;
        this.meterRegistry = meterRegistry;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public MetricsResponse getApplicationMetrics() {
        // Get total requests from counter
        long totalRequests = (long) meterRegistry.counter("weather_requests_total").count();

        // Get average response time from last hour
        Double averageResponseTime = weatherDataRepository.getAverageResponseTime(
                LocalDateTime.now().minusHours(1)
        );

        // Get active threads
        long activeThreads = threadMXBean.getThreadCount();

        // Get memory usage in MB
        long usedMemory = memoryMXBean.getHeapMemoryUsage().getUsed();
        double memoryUsageMB = usedMemory / (1024.0 * 1024.0);

        // CPU usage (simplified - would need more complex implementation for accurate CPU monitoring)
        double cpuUsagePercent = getCpuUsage();

        return new MetricsResponse(
                totalRequests,
                averageResponseTime != null ? averageResponseTime : 0.0,
                activeThreads,
                memoryUsageMB,
                cpuUsagePercent
        );
    }

    private double getCpuUsage() {
        // Simplified CPU usage calculation
        // In production, you might want to use OperatingSystemMXBean
        return Math.random() * 100; // Placeholder
    }
}