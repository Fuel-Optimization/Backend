package com.example.ChartsService.service;

import com.example.model.Repository.DriverRecordRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
}
