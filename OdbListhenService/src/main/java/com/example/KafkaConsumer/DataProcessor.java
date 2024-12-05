package com.example.KafkaConsumer;

import com.example.mappers.DriverRecordMapper;
import com.example.mappers.ModelAttributesMapper;
import com.example.model.model.DriverRecord;
import com.example.model.model.ModelAttributes;
import com.example.service.DriverRecordService;
import com.example.service.PredictionService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DataProcessor {

    private final PredictionService predictionService;
    private final DriverRecordService driverRecordService;
    private final ModelAttributesMapper modelAttributesMapper;
    private final DriverRecordMapper driverRecordMapper;
    private final Random random;
    private final List<String> carLicenseIds;

    public DataProcessor(PredictionService predictionService,
                         DriverRecordService driverRecordService,
                         ModelAttributesMapper modelAttributesMapper,
                         DriverRecordMapper driverRecordMapper) {
        this.predictionService = predictionService;
        this.driverRecordService = driverRecordService;
        this.modelAttributesMapper = modelAttributesMapper;
        this.driverRecordMapper = driverRecordMapper;
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

    @PostConstruct
    public void init() {
        generateAndProcessData();
    }

    private void generateAndProcessData() {
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();
        List<LocalDateTime> allDays = getAllDaysBetween(startDate, endDate);

        for (LocalDateTime day : allDays) {
            for (int driverId = 11; driverId <=20 ; driverId++) {
                for (int recordCount = 0; recordCount < 3; recordCount++) {
                    // Generate mock data
                    ModelAttributes mockData = generateMockData();

                    // Add metadata
                    Map<String, Object> data = mockData.toMap();
                    data.put("Driver_ID", driverId);
                    data.put("Car_ID", getCarLicenseIdForDriver(driverId));
                    data.put("Date", getRandomTimestampForDay(day));

                    // Process and save data
                    processAndSaveData(data);
                }
            }
        }
    }

    private void processAndSaveData(Map<String, Object> data) {
        try {
            ModelAttributes modelAttributes = modelAttributesMapper.map(data);
            Map<String, Object> predictionResponse = predictionService.getPrediction(modelAttributes);
            DriverRecord driverRecord = driverRecordMapper.map(data, modelAttributes, predictionResponse);

            driverRecordService.saveDriverRecord(driverRecord);
//            System.out.println("Saved to database: " + driverRecord);
        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
        }
    }

    private List<LocalDateTime> getAllDaysBetween(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime> days = new ArrayList<>();
        LocalDateTime currentDay = start;

        while (!currentDay.isAfter(end)) {
            days.add(currentDay);
            currentDay = currentDay.plusDays(1);
        }

        return days;
    }

    private String getRandomTimestampForDay(LocalDateTime day) {
        int randomHour = random.nextInt(24);
        int randomMinute = random.nextInt(60);
        int randomSecond = random.nextInt(60);

        LocalDateTime randomTime = day.withHour(randomHour).withMinute(randomMinute).withSecond(randomSecond);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return randomTime.format(formatter);
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

    private String getCarLicenseIdForDriver(int driverId) {
        return carLicenseIds.get(driverId - 1);
    }
}
