package com.example.Kafka.Producers;

import com.example.model.model.ModelAttributes;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class KafkaScheduler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Random random;
    private final List<String> carLicenseIds;

    public KafkaScheduler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.random = new Random();
        this.carLicenseIds = List.of(
                "CAR-1-4536", "CAR-2-7893", "CAR-3-2345", "CAR-4-9823", "CAR-5-5647",
                "CAR-6-3942", "CAR-7-1427", "CAR-8-8765", "CAR-9-2954", "CAR-10-6584",
                "CAR-11-8974", "CAR-12-2958", "CAR-13-6842", "CAR-14-4329", "CAR-15-1726",
                "CAR-16-8901", "CAR-17-5632", "CAR-18-1947", "CAR-19-7843", "CAR-20-2147",
                "CAR-21-5392", "CAR-22-8234", "CAR-23-6547", "CAR-24-9438", "CAR-25-3847",
                "CAR-26-5849", "CAR-27-7350", "CAR-28-9824", "CAR-29-5421", "CAR-30-6732"
        );
    }

    @Scheduled(fixedRate = 10000)
    //@Scheduled(fixedRate = 60000) for 1 minute
    public void sendMockData() {
        for (int driverId = 1; driverId <= 20; driverId++) {
            // Create mock data for each driver
            ModelAttributes mockData = generateMockData();

            // Add additional driver-specific fields
            Map<String, Object> data = mockData.toMap();
            data.put("Driver_ID", driverId);
            data.put("Car_ID", getCarLicenseIdForDriver(driverId));
            data.put("Date", getCurrentTimestamp());

            String jsonString = convertToJson(data);


            kafkaTemplate.send(new ProducerRecord<>("FuelOpt", jsonString));
            System.out.println("Sent mock data to Kafka for Driver_ID " + driverId + ": " + jsonString);
        }
    }

    private ModelAttributes generateMockData() {
        ModelAttributes mockData = new ModelAttributes();
        mockData.setEngineSpeed(600 + random.nextInt(2200));
        mockData.setVehicleSpeed(random.nextInt(80));
        mockData.setAcceleratorPedalValue(random.nextInt(2));
        mockData.setIntakeAirPressure(random.nextInt(80));
        mockData.setAccelerationSpeedLongitudinal(random.nextDouble() * (3 - (-2)) + (-2));
        mockData.setMinimumIndicatedEngineTorque(random.nextInt(3));
        mockData.setIndicationOfBrakeSwitchOnOff(random.nextBoolean() ? 1 : 0);
        mockData.setConverterClutch(random.nextBoolean() ? 1 : 0);
        mockData.setEngineIdleTargetSpeed(random.nextInt(1200 - 630 + 1) + 630);
        mockData.setCurrentSparkTiming(random.nextInt(30 - (-6) + 1) + (-6));
        mockData.setMasterCylinderPressure(random.nextDouble(14));
        mockData.setTorqueOfFriction(random.nextInt(17 - 7 + 1) + 7);
        mockData.setEngineInFuelCutOff(random.nextBoolean() ? 1 : 0);
        mockData.setCurrentGear(random.nextInt(6));
        mockData.setCalculatedRoadGradient(random.nextDouble() * (4 - (-3)) + (-3));
        mockData.setLongTermFuelTrimBank1(random.nextDouble() * (-0.8 - 4) + 4);

        return mockData;
    }

    private String convertToJson(Map<String, Object> data) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        data.forEach((key, value) -> {
            jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\", ");
        });
        jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private String getCarLicenseIdForDriver(int driverId) {
        return carLicenseIds.get(driverId - 1); // Map driver ID to car license ID
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
