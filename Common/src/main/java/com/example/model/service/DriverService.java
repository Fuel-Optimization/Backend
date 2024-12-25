package com.example.model.service;
import com.example.model.Repository.DriverRecordRepository;
import com.example.model.Repository.DriverRepository;
import com.example.model.dto.DriverDto;
import com.example.model.dto.UserDTO;
import com.example.model.model.Driver;
import com.example.model.model.DriverRecord;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DriverService {

    private final DriverRepository driverRepository;

    private final DriverRecordRepository driverRecordRepository;


    public DriverService(DriverRepository driverRepository ,  DriverRecordRepository driverRecordRepository) {
        this.driverRepository = driverRepository;
        this.driverRecordRepository = driverRecordRepository;
    }

    public List<DriverDto> searchByName(String name) {
        List<Driver> drivers = driverRepository.findByName(name);
        return drivers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public List<DriverDto> searchByNationalId(String nationalId) {
        List<Driver> drivers = driverRepository.findByNationalId(nationalId);
        return drivers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DriverDto convertToDTO(Driver driver) {
        DriverDto driverDTO = new DriverDto();
        driverDTO.setId(driver.getId());
        driverDTO.setYearsOfExperience(driver.getYearsOfExperience());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(driver.getUser().getId());
        userDTO.setFirstName(driver.getUser().getFirstName());
        userDTO.setLastName(driver.getUser().getLastName());
        userDTO.setFamilyName(driver.getUser().getFamilyName());
        userDTO.setEmail(driver.getUser().getEmail());
        userDTO.setPhoneNumber(driver.getUser().getPhoneNumber());
        userDTO.setNationalid(driver.getUser().getNationalid());
        driverDTO.setUser(userDTO);
        return driverDTO;
    }

    public Driver getDriverById(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
    }

    public Driver getDriverByUserId(Long userId) throws ResourceNotFoundException {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with User ID: " + userId));
    }

    public List<Map<String, Object>> getDriverDetails(List<Driver> drivers) {
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DAY_OF_YEAR, -14); // Two weeks ago
        Date startDate = calendar.getTime();

        return drivers.stream().map(driver -> {
            //List<DriverRecord> records = driverRecordRepository.findRecordsByDriverIdAndDateRange(driver.getId(), startDate, endDate);
            List<DriverRecord> records = driverRecordRepository.findRecordsByDriverId(driver.getId());
            double avgFuelConsumption = records.stream()
                    .mapToDouble(DriverRecord::getPredictedFuelConsumption)
                    .average()
                    .orElse(0);

            // Round the average fuel consumption to two decimal places
            avgFuelConsumption = BigDecimal.valueOf(avgFuelConsumption)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            String status;
            if (avgFuelConsumption < 10) {
                status = "excellent";
            } else if (avgFuelConsumption <= 15) {
                status = "good";
            } else {
                status = "poor";
            }

            Map<String, Object> driverDetails = new HashMap<>();
            driverDetails.put("id", driver.getId());
            driverDetails.put("name", driver.getUser().getFirstName() + " " + driver.getUser().getLastName());
            driverDetails.put("email", driver.getUser().getEmail()); // Add email
            driverDetails.put("mobile", driver.getUser().getPhoneNumber()); // Add mobile number
            driverDetails.put("yearsOfExperience", driver.getYearsOfExperience());
            driverDetails.put("avgFuelConsumption", avgFuelConsumption);
            driverDetails.put("status", status);

            return driverDetails;
        }).collect(Collectors.toList());
    }





    public List<Map<String, Object>> getDriverCombinedAverages(Long driverId) {
        List<DriverRecord> records = driverRecordRepository.findByDriverId(driverId);

        // Group by week
        Map<Integer, List<DriverRecord>> recordsByWeek = records.stream().collect(Collectors.groupingBy(record -> {
            LocalDate date = record.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return date.get(WeekFields.ISO.weekOfWeekBasedYear());
        }));

        List<Map<String, Object>> combinedAverages = new ArrayList<>();
        recordsByWeek.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            double average = Math.round(entry.getValue().stream()
                    .mapToDouble(DriverRecord::getPredictedFuelConsumption)
                    .average().orElse(0) * 100.0) / 100.0;
            Map<String, Object> data = new HashMap<>();
            data.put("weekName", "Week " + entry.getKey());
            data.put("averageFuelConsumption", average);
            combinedAverages.add(data);
        });

        // Group by month
        Map<String, List<DriverRecord>> recordsByMonth = records.stream().collect(Collectors.groupingBy(record -> {
            LocalDate date = record.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + date.getYear();
        }));

        recordsByMonth.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            double average = entry.getValue().stream()
                    .mapToDouble(DriverRecord::getPredictedFuelConsumption)
                    .average().orElse(0);
            Map<String, Object> data = new HashMap<>();
            data.put("monthName", entry.getKey());
            data.put("averageFuelConsumption", average);
            combinedAverages.add(data);
        });

        return combinedAverages;
    }


    public Map<String, Object> getDriverSummary(Long driverId) {
        List<DriverRecord> records = driverRecordRepository.findByDriverId(driverId);

        double optimalEngineSpeed = 1500.0; // Optimal engine speed (RPM)
        double maxVehicleSpeed = 120.0; // Maximum vehicle speed for normalization
        double maxTorque = 100.0; // Maximum torque for normalization

        // Calculate weighted contributions
        double engineSpeedEfficiency = records.stream()
                .mapToDouble(record -> {
                    double deviation = Math.abs(record.getEngineSpeed() - optimalEngineSpeed) / optimalEngineSpeed;
                    return Math.max(0, 1 - deviation); // Normalize between 0 and 1
                })
                .average()
                .orElse(0);

        double vehicleSpeedEfficiency = records.stream()
                .mapToDouble(record -> Math.min(1.0, record.getVehicleSpeed() / maxVehicleSpeed)) // Normalize
                .average()
                .orElse(0);

        double torqueEfficiency = records.stream()
                .mapToDouble(record -> Math.max(0, 1 - record.getTorqueOfFriction() / maxTorque)) // Normalize
                .average()
                .orElse(0);

        double fuelEfficiency = records.stream()
                .mapToDouble(record -> 1 / Math.max(1, record.getPredictedFuelConsumption())) // Inverse relationship
                .average()
                .orElse(0);

        double clutchEfficiency = records.stream()
                .mapToDouble(DriverRecord::getConverterClutchContribution)
                .average()
                .orElse(0);

        // Combine all weighted factors
        double engineEfficiency = (0.3 * engineSpeedEfficiency +
                0.25 * vehicleSpeedEfficiency +
                0.2 * torqueEfficiency +
                0.15 * fuelEfficiency +
                0.1 * clutchEfficiency) * 100;

        // Prepare the response
        double predictedFuelConsumption = Math.round(
                records.stream().mapToDouble(DriverRecord::getPredictedFuelConsumption).average().orElse(0) * 100.0) / 100.0;
        double averageSpeed = Math.round(
                records.stream().mapToDouble(DriverRecord::getVehicleSpeed).average().orElse(0) * 100.0) / 100.0;
        double maxSpeed = records.stream().mapToDouble(DriverRecord::getVehicleSpeed).max().orElse(0);

        Map<String, Object> summary = new HashMap<>();
        summary.put("predictedFuelConsumption", predictedFuelConsumption);
        summary.put("averageSpeed", averageSpeed);
        summary.put("maxSpeed", maxSpeed);
        summary.put("engineEfficiency", Math.round(engineEfficiency * 100.0) / 100.0); // Round to 2 decimals

        return summary;
    }



    public Map<String, Object> getAttributeData(Long driverId, String attribute) {
        // Temporary hardcoded dates for debugging
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.AUGUST, 31); // Set start date to August 31, 2024
        Date startDate = calendar.getTime();

        calendar.set(2024, Calendar.SEPTEMBER, 7); // Set end date to September 7, 2024
        Date endDate = calendar.getTime();

        // Fetch records within the date range
        List<DriverRecord> records = driverRecordRepository.findRecordsByDriverIdAndDateRange(driverId, startDate, endDate);

        // Generate sequential labels (1, 2, 3, ...)
        List<String> labels = new ArrayList<>();
        for (int i = 1; i <= records.size(); i++) {
            labels.add(String.valueOf(i));
        }

        // Extract the specified attribute values dynamically
        List<Double> values = records.stream()
                .map(record -> getAttributeValue(record, attribute))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Calculate summary statistics with rounding
        double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        min = roundToTwoDecimals(min);
        max = roundToTwoDecimals(max);
        average = roundToTwoDecimals(average);

        // Build the response map
        Map<String, Object> response = new HashMap<>();
        response.put("attribute", attribute);
        response.put("labels", labels);
        response.put("values", values);
        response.put("summary", Map.of(
                "min", min,
                "max", max,
                "average", average
        ));

        // Debugging Output
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Fetched Records: " + records.size());

        return response;
    }

    // Helper method to round a double value to two decimal places
    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }


    // Helper method to extract the attribute value dynamically
    private Double getAttributeValue(DriverRecord record, String attribute) {
        switch (attribute) {
            case "engineSpeed": return (double) record.getEngineSpeed();
            case "vehicleSpeed": return (double) record.getVehicleSpeed();
            case "acceleratorPedalValue": return record.getAcceleratorPedalValue();
            case "intakeAirPressure": return (double) record.getIntakeAirPressure();
            case "accelerationSpeedLongitudinal": return record.getAccelerationSpeedLongitudinal();
            case "minimumIndicatedEngineTorque": return (double) record.getMinimumIndicatedEngineTorque();
            case "indicationOfBrakeSwitchOnOff": return (double) record.getIndicationOfBrakeSwitchOnOff();
            case "converterClutch": return (double) record.getConverterClutch();
            case "engineIdleTargetSpeed": return (double) record.getEngineIdleTargetSpeed();
            case "currentSparkTiming": return (double) record.getCurrentSparkTiming();
            case "masterCylinderPressure": return record.getMasterCylinderPressure();
            case "torqueOfFriction": return (double) record.getTorqueOfFriction();
            case "engineInFuelCutOff": return (double) record.getEngineInFuelCutOff();
            case "currentGear": return (double) record.getCurrentGear();
            case "calculatedRoadGradient": return record.getCalculatedRoadGradient();
            case "longTermFuelTrimBank1": return record.getLongTermFuelTrimBank1();
            default: return null;
        }
    }

}
