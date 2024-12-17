package com.example.ChartsService.service;

import com.example.model.Repository.DriverRecordRepository;
import com.example.model.Repository.DriverRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class chartService {
    private DriverRecordRepository driverRecordRepository;
    private  DriverRepository driverRepository;

    public chartService(DriverRecordRepository driverRecordRepository, DriverRepository driverRepository) {
        this.driverRecordRepository = driverRecordRepository;
        this.driverRepository = driverRepository;
    }

    public Map<Integer, Double> getAverageFuelConsumptionByTop5ForManager(Long managerId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByTop5ForManager(managerId, startDate, endDate);

        Map<Integer, Double> fuelConsumptionMap = new LinkedHashMap<>();
        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            Double avgFuelConsumption = (Double) result[1];
            fuelConsumptionMap.put(driverId, avgFuelConsumption);
        }
        return fuelConsumptionMap;
    }

    public  Map<String,Double> getAverageFuelConsumptionByManagers(Long managerId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByDriverAndManager(managerId, startDate, endDate );
        Map<String,  Double> fuelConsumptionMap = new LinkedHashMap<>();

        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            String name = driverRepository.findById(Long.valueOf(driverId))
                    .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId)).getUser().getFirstName();
            Double avgFuelConsumption = (Double) result[1];
            fuelConsumptionMap.put(name, avgFuelConsumption);
        }
        return fuelConsumptionMap;
    }

    public   Map<String, Integer> getAverageFuelConsumptionClassificationByManagers(Long managerId, Date startDate, Date endDate) {
        Map<String, Integer> classificationCount = new HashMap<>();
        Map<String,  Double> fuelConsumptionMap = getAverageFuelConsumptionByManagers(managerId,startDate,endDate);
        for (Map.Entry<String,Double> entry : fuelConsumptionMap.entrySet()){
            Double avgFuelConsumption = entry.getValue();
            String classification;
            if (avgFuelConsumption<=10){
                classification ="Excellent ";
            } else if (avgFuelConsumption>10 && avgFuelConsumption <=12) {
                classification = "Moderate";
            }else {
                classification = "Poor";
            }

            classificationCount.put(classification, classificationCount.getOrDefault(classification, 0) + 1);
        }
        return classificationCount;
    }

    public Pair<Date,Date> setDefaultDates(String startDate, String endDate) throws ParseException {
        Date start = null;
        Date end = null;

        Pair<Date,Date> dates = null;

        // If startDate and endDate are not provided, default to last week's data
        if (startDate == null && endDate == null) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            start = calendar.getTime();

            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            end = calendar.getTime();
        } else {
            // If startDate and endDate are provided, parse and filter by those dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        }

        dates = Pair.of(start,end);
        return dates;
    }

    public Map<String, Object> getGroupedConsumption() {
        Map<String, Object> result = new HashMap<>();

//        // Weekly Data
//        List<Object[]> weeklyData = driverRecordRepository.getWeeklyConsumption();
//        result.put("weekly", weeklyData);
//
//        // Monthly Data
//        List<Object[]> monthlyData = driverRecordRepository.getMonthlyConsumption();
//        result.put("monthly", monthlyData);

        // Yearly Data
        List<Object[]> yearlyData = driverRecordRepository.getYearlyConsumption();
        result.put("yearly", yearlyData);

        return result;
    }
}
