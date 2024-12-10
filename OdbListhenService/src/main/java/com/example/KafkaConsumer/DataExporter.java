package com.example.KafkaConsumer;

import com.example.mappers.DriverRecordMapper;
import com.example.mappers.ModelAttributesMapper;
import com.example.model.model.DriverRecord;
import com.example.model.model.ModelAttributes;
import com.example.service.DriverRecordService;
import com.example.service.PredictionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DataExporter {

    private final Random random;
    private final List<String> carLicenseIds;
    private final PredictionService predictionService;
    private final DriverRecordService driverRecordService;
    private final ModelAttributesMapper modelAttributesMapper;
    private final DriverRecordMapper driverRecordMapper;
    public DataExporter(
            PredictionService predictionService, DriverRecordService driverRecordService, ModelAttributesMapper modelAttributesMapper, DriverRecordMapper driverRecordMapper) {
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
                "CAR-21-1111","CAR-22-1111","CAR-23-1111","CAR-24-1111","CAR-25-1111"

        );
    }

    @PostConstruct
    public void init() {
        try {
            generateAndExportDataToCSV();
        } catch (Exception e) {
            System.err.println("Error generating or writing CSV: " + e.getMessage());
        }
    }

    private void generateAndExportDataToCSV() {

            LocalDateTime startDate = LocalDateTime.of(2024, 9, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 23, 59); // Date range for generating data
            LocalDateTime currentDay = startDate;

            while (!currentDay.isAfter(endDate)) {
                int randomHour = random.nextInt(24);
                int randomMinute = random.nextInt(60);
                LocalDateTime randomStartTime = currentDay.withHour(randomHour).withMinute(randomMinute).withSecond(0);
                for (int i = 0; i < 30; i++) {
                    LocalDateTime currentInterval = randomStartTime.plusMinutes(i);
                    if (currentInterval.getDayOfMonth() != currentDay.getDayOfMonth()) {
                        break;
                    }
                    for (int driverId = 21; driverId <= 22; driverId++) {ModelAttributes mockData = generateMockData();Map<String, Object> data = mockData.toMap();
                    data.put("Driver_ID", driverId);
                    data.put("Car_ID", getCarLicenseIdForDriver(driverId));
                    data.put("Date", currentInterval);
                    processAndSaveData(data);
                    }
                }
                currentDay = currentDay.plusDays(1);
            }
        System.out.println("Finshed");
        }


        private void processAndSaveData(Map<String, Object> data) {
        try {
            ModelAttributes modelAttributes = modelAttributesMapper.map(data);
            Map<String, Object> predictionResponse = predictionService.getPrediction(modelAttributes);
            DriverRecord driverRecord = driverRecordMapper.map(data, modelAttributes, predictionResponse);
            driverRecordService.saveDriverRecord(driverRecord);

        } catch (Exception e) {
            System.err.println("Error processing data: " + e.getMessage());
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
        mockData.setMasterCylinderPressure(random.nextDouble() * 14);
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
