//package com.example.Kafka.Producers;
//
//import com.example.model.ModelAttributes;
//import jakarta.annotation.PostConstruct;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//@Component
//public class ScriptGenerator {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final Random random;
//    private final List<String> carLicenseIds;
//
//    public ScriptGenerator(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.random = new Random();
//        this.carLicenseIds = List.of(
//                "CAR-1-4536",
//                "CAR-2-7893",
//                "CAR-3-2345",
//                "CAR-4-9823",
//                "CAR-5-5647",
//                "CAR-6-3942",
//                "CAR-7-1427",
//                "CAR-8-8765",
//                "CAR-9-2954",
//                "CAR-10-6584"
//        );
//    }
//
//    @PostConstruct
//    public void init() {
//        // Uncomment the line below if you want this method to run on startup
//         sendMockData();
//    }
//    public void sendMockData() {
//        // Define the start and end dates
//        LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 0, 0);
//        LocalDateTime endDate = LocalDateTime.now();
//
//        // Generate a list of all days from start to end
//        List<LocalDateTime> allDays = getAllDaysBetween(startDate, endDate);
//
//        for (LocalDateTime day : allDays) {
//            for (int driverId = 1; driverId <= 10; driverId++) {
//                // Ensure 5 records per day for each driver
//                for (int recordCount = 0; recordCount < 5; recordCount++) {
//                    ModelAttributes mockData = generateMockData();
//
//                    Map<String, Object> data = mockData.toMap();
//                    data.put("Driver_ID", driverId);
//                    data.put("Car_ID", getCarLicenseIdForDriver(driverId));
//                    data.put("Date", getRandomTimestampForDay(day));
//
//                    String jsonString = convertToJson(data);
//
//                    kafkaTemplate.send(new ProducerRecord<>("GpScript", jsonString));
//                    System.out.println("Sent mock data to Kafka for Driver_ID " + driverId + ": " + jsonString);
//                }
//            }
//        }
//    }
//
//    private List<LocalDateTime> getAllDaysBetween(LocalDateTime start, LocalDateTime end) {
//        List<LocalDateTime> days = new ArrayList<>();
//        LocalDateTime currentDay = start;
//
//        while (!currentDay.isAfter(end)) {
//            days.add(currentDay);
//            currentDay = currentDay.plusDays(1);
//        }
//
//        return days;
//    }
//
//    private String getRandomTimestampForDay(LocalDateTime day) {
//        // Generate a random timestamp within the given day
//        int randomHour = random.nextInt(24);
//        int randomMinute = random.nextInt(60);
//        int randomSecond = random.nextInt(60);
//
//        LocalDateTime randomTime = day.withHour(randomHour).withMinute(randomMinute).withSecond(randomSecond);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return randomTime.format(formatter);
//    }
//
//    private ModelAttributes generateMockData() {
//        ModelAttributes mockData = new ModelAttributes();
//        mockData.setEngineSpeed(random.nextInt(1400));
//        mockData.setVehicleSpeed(random.nextInt(80));
//        mockData.setAcceleratorPedalValue(random.nextInt(2));
//        mockData.setIntakeAirPressure(random.nextInt(80));
//        mockData.setAccelerationSpeedLongitudinal(random.nextDouble() * (3 - (-2)) + (-2));
//        mockData.setMinimumIndicatedEngineTorque(random.nextInt(3));
//        mockData.setIndicationOfBrakeSwitchOnOff(random.nextBoolean() ? 1 : 0);
//        mockData.setConverterClutch(random.nextBoolean() ? 1 : 0);
//        mockData.setEngineIdleTargetSpeed(random.nextInt(1200 - 630 + 1) + 630);
//        mockData.setCurrentSparkTiming(random.nextInt(30 - (-6) + 1) + (-6));
//        mockData.setMasterCylinderPressure(random.nextDouble(14));
//        mockData.setTorqueOfFriction(random.nextInt(17 - 7 + 1) + 7);
//        mockData.setEngineInFuelCutOff(random.nextBoolean() ? 1 : 0);
//        mockData.setCurrentGear(random.nextInt(6));
//        mockData.setCalculatedRoadGradient(random.nextDouble() * (4 - (-3)) + (-3));
//        mockData.setLongTermFuelTrimBank1(random.nextDouble() * (-0.8 - 4) + 4);
//
//        return mockData;
//    }
//
//    private String convertToJson(Map<String, Object> data) {
//        StringBuilder jsonBuilder = new StringBuilder();
//        jsonBuilder.append("{");
//        data.forEach((key, value) -> {
//            jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\", ");
//        });
//        jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
//        jsonBuilder.append("}");
//        return jsonBuilder.toString();
//    }
//
//    private String getCarLicenseIdForDriver(int driverId) {
//        return carLicenseIds.get(driverId - 1); // Map driver ID to car license ID
//    }
//}
