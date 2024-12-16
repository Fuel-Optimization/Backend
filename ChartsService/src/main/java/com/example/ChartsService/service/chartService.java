package com.example.ChartsService.service;

import com.example.ChartsService.dto.AverageContributionsDTO;
import com.example.ChartsService.dto.DriverBehaviorDTO;
import com.example.ChartsService.dto.PredictedFuelConsumptionDTO;
import com.example.model.Repository.DriverRecordRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class chartService {
    private DriverRecordRepository driverRecordRepository;
    public chartService(DriverRecordRepository driverRecordRepository) {
        this.driverRecordRepository = driverRecordRepository;
    }

    public Map<Integer, Double> getAverageFuelConsumptionByManagersDate(Long managerId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByDriverAndManagerDate(managerId, startDate, endDate);

        // Map driverId to average fuel consumption
        Map<Integer, Double> fuelConsumptionMap = new LinkedHashMap<>();
        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            Double avgFuelConsumption = (Double) result[1];
            fuelConsumptionMap.put(driverId, avgFuelConsumption);
        }
        return fuelConsumptionMap;
    }

    public  Map<Integer, Pair<Double, String>> getAverageFuelConsumptionByManagers(Long managerId) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByDriverAndManager(managerId);

        // Map driverId to average fuel consumption
        Map<Integer, Pair<Double, String>> fuelConsumptionMap = new LinkedHashMap<>();
        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            Double avgFuelConsumption = (Double) result[1];
            String classification;
            if (avgFuelConsumption<=10){
                classification ="Excellent ";
            } else if (avgFuelConsumption>10 && avgFuelConsumption <=12) {
                classification = "Moderate";
            }else {
                classification = "Poor";
            }
            fuelConsumptionMap.put(driverId, Pair.of(avgFuelConsumption, classification));
        }
        return fuelConsumptionMap;
    }

    public AverageContributionsDTO getAverageAttributesContributionsByDriverAndDate(Long driverId, Date startDate, Date endDate){
        Object[] result = driverRecordRepository.findAverageAttributesContributionsByDriverAndDate(driverId, startDate, endDate).get(0);
        return AverageContributionsDTO.builder()
                .engineSpeedContribution((Double) result[0])
                .vehicleSpeedContribution((Double) result[1])
                .acceleratorPedalValueContribution((Double) result[2])
                .intakeAirPressureContribution((Double) result[3])
                .accelerationSpeedLongitudinalContribution((Double) result[4])
                .minimumIndicatedEngineTorqueContribution((Double) result[5])
                .indicationOfBrakeSwitchOnOffContribution((Double) result[6])
                .converterClutchContribution((Double) result[7])
                .engineIdleTargetSpeedContribution((Double) result[8])
                .currentSparkTimingContribution((Double) result[9])
                .masterCylinderPressureContribution((Double) result[10])
                .torqueOfFrictionContribution((Double) result[11])
                .engineInFuelCutOffContribution((Double) result[12])
                .currentGearContribution((Double) result[13])
                .calculatedRoadGradientContribution((Double) result[14])
                .longTermFuelTrimBank1Contribution((Double) result[15])
                .build();
    }

    public List<PredictedFuelConsumptionDTO> getPredictedFuelConsumptionByDriverAndDate(Long driverId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findPredictedFuelConsumptionByDriverAndDate(driverId, startDate, endDate);
        List<PredictedFuelConsumptionDTO> predictedFuelConsumptionDTOs = new ArrayList<PredictedFuelConsumptionDTO>();
        for (Object[] result : results) {
            predictedFuelConsumptionDTOs.add(PredictedFuelConsumptionDTO.builder()
                    .predictedFuelConsumption((Double) result[0])
                    .time((Date) result[1])
                    .build()
            );
        }
        return predictedFuelConsumptionDTOs;
    }

    public List<PredictedFuelConsumptionDTO> getDailyAveragePredictedFuelConsumptionByDriverAndDate(Long driverId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findDailyAveragePredictedFuelConsumptionByDriverAndDate(driverId, startDate, endDate);
        List<PredictedFuelConsumptionDTO> dailyAveragePredictedFuelConsumptionDTOs = new ArrayList<PredictedFuelConsumptionDTO>();
        for (Object[] result : results) {
            dailyAveragePredictedFuelConsumptionDTOs.add(PredictedFuelConsumptionDTO.builder()
                    .predictedFuelConsumption((Double) result[0])
                    .time((Date) result[1])
                    .build()
            );
        }
        return dailyAveragePredictedFuelConsumptionDTOs;
    }

    public List<DriverBehaviorDTO> getDriverBehavior(Long driverId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findPredictedFuelConsumptionByDriverAndDate(driverId, startDate, endDate);
        List<DriverBehaviorDTO> driverBehaviorDTOs = new ArrayList<DriverBehaviorDTO>();
        for (Object[] result : results) {
            Double predictedFuelConsumption = (Double) result[0];
            String classification="";
            if (predictedFuelConsumption<=10){
                classification ="Excellent ";
            } else if (predictedFuelConsumption>10 && predictedFuelConsumption <=12) {
                classification = "Moderate";
            }else {
                classification = "Poor";
            }
            driverBehaviorDTOs.add(DriverBehaviorDTO.builder()
                    .driverBehavior(classification)
                    .time((Date) result[1])
                    .build()
            );
        }
        return driverBehaviorDTOs;
    }

    public List<DriverBehaviorDTO> getDailyDriverBehavior(Long driverId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findDailyAveragePredictedFuelConsumptionByDriverAndDate(driverId, startDate, endDate);
        List<DriverBehaviorDTO> dailyDriverBehaviorDTOs = new ArrayList<DriverBehaviorDTO>();
        for (Object[] result : results) {
            Double predictedFuelConsumption = (Double) result[0];
            String classification="";
            if (predictedFuelConsumption<=10){
                classification ="Excellent ";
            } else if (predictedFuelConsumption>10 && predictedFuelConsumption <=12) {
                classification = "Moderate";
            }else {
                classification = "Poor";
            }
            dailyDriverBehaviorDTOs.add(DriverBehaviorDTO.builder()
                    .driverBehavior(classification)
                    .time((Date) result[1])
                    .build()
            );
        }
        return dailyDriverBehaviorDTOs;
    }
}
