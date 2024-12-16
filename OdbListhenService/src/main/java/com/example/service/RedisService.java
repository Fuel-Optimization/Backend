package com.example.service;

import com.example.model.Repository.AlertRepository;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.Alert;
import com.example.model.model.Driver;
import com.example.model.model.Manager;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final DriverRepository driverRepository;
    private final AlertRepository alertRepository;
    private final EmailService emailService;

    public RedisService(RedisTemplate<String, String> redisTemplate,
                        DriverRepository driverRepository,
                        AlertRepository alertRepository,
                        EmailService emailService) {
        this.redisTemplate = redisTemplate;
        this.driverRepository = driverRepository;
        this.alertRepository = alertRepository;
        this.emailService = emailService;
    }

    @PostConstruct
    public void clearRedisOnStartup() {
        try {
            // Deletes all keys in Redis
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            System.out.println("Redis database cleared on application startup.");
        } catch (Exception e) {
            System.err.println("Failed to clear Redis database: " + e.getMessage());
        }
    }

     public void saveFuelConsumption(int driverId, double fuelConsumption) {
        String key = "fuel:consumption:" + driverId;
        long timestamp = Instant.now().toEpochMilli();
        redisTemplate.opsForZSet().add(key, String.valueOf(fuelConsumption), timestamp);
        Long recordCount = redisTemplate.opsForZSet().zCard(key);
        if (recordCount != null && recordCount >= 5) {
            processRecordsForKey(driverId, key);
            redisTemplate.opsForZSet().removeRange(key, 0, 4);
        }
    }

    private void processRecordsForKey(int driverId, String key) {
        Set<String> consumptions = redisTemplate.opsForZSet().range(key, 0, -1);
        if (consumptions == null || consumptions.isEmpty()) {
            return;
        }
        double avgFuelConsumption = consumptions.stream()
                .mapToDouble(Double::parseDouble)
                .average()
                .orElse(0.0);

        if (avgFuelConsumption > 12) {
            Driver driver = driverRepository.findById((long) driverId).orElse(null);
            if (driver != null && driver.getManager() != null) {
                Manager manager = driver.getManager();
                String DriverFullName = driver.getUser().getFirstName() + " " +
                        driver.getUser().getFamilyName();
                Alert alert = new Alert(
                        0L,
                        String.valueOf(driverId),
                        avgFuelConsumption,
                        "Driver " + DriverFullName +
                                " has consumed more than " + avgFuelConsumption +
                                " liters per unit over the recent period.",
                        LocalDateTime.now(),
                        manager
                );
                alertRepository.save(alert);
                emailService.TriggerEmail(manager, DriverFullName, avgFuelConsumption);
            }
        }
    }
}
