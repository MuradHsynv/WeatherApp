package com.weatherapp.dto;

public class MetricsResponse {
    private long totalRequests;
    private double averageResponseTime;
    private long activeThreads;
    private double memoryUsageMB;
    private double cpuUsagePercent;

    // Constructors
    public MetricsResponse() {}

    public MetricsResponse(long totalRequests, double averageResponseTime,
                           long activeThreads, double memoryUsageMB, double cpuUsagePercent) {
        this.totalRequests = totalRequests;
        this.averageResponseTime = averageResponseTime;
        this.activeThreads = activeThreads;
        this.memoryUsageMB = memoryUsageMB;
        this.cpuUsagePercent = cpuUsagePercent;
    }

    // Getters and Setters
    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

    public double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }

    public long getActiveThreads() { return activeThreads; }
    public void setActiveThreads(long activeThreads) { this.activeThreads = activeThreads; }

    public double getMemoryUsageMB() { return memoryUsageMB; }
    public void setMemoryUsageMB(double memoryUsageMB) { this.memoryUsageMB = memoryUsageMB; }

    public double getCpuUsagePercent() { return cpuUsagePercent; }
    public void setCpuUsagePercent(double cpuUsagePercent) { this.cpuUsagePercent = cpuUsagePercent; }
}