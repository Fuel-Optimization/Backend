package com.example.ChartsService.service;

import com.example.ChartsService.dto.*;
import com.example.model.Repository.DriverRecordRepository;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.User;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class chartService {
    private DriverRecordRepository driverRecordRepository;
    private  DriverRepository driverRepository;

    public chartService(DriverRecordRepository driverRecordRepository, DriverRepository driverRepository) {
        this.driverRecordRepository = driverRecordRepository;
        this.driverRepository = driverRepository;
    }

    public Double getOverAllAverageFuelConsumption(Long managerId){
        Object result = driverRecordRepository.findAverageFuelConsumptionOfDrivers(managerId);
        return (Double)result;
    }


    public List<DriverInfoDTO> getAverageFuelConsumptionByTop5ForManagerNoDate(Long managerId) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByTop5ForManagerNoDate(managerId);

        //A map to store the driverId and DriverInfoDTO
        List<DriverInfoDTO> fuelConsumptionList = new ArrayList<>();
        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            User user = driverRepository.findById(Long.valueOf(driverId))
                    .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId)).getUser();
            DriverInfoDTO driverInfoDTO = getDriverInfoDTO(driverId,(Double)result[1], user);
            fuelConsumptionList.add(driverInfoDTO);
        }
        return fuelConsumptionList;
    }


    public List<DriverInfoDTO> getAverageFuelConsumptionByTop5ForManager(Long managerId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByTop5ForManager(managerId, startDate, endDate);

        List<DriverInfoDTO> fuelConsumptionMap = new ArrayList<>();
        for (Object[] result : results) {
            Integer driverId = (Integer) result[0];
            User user = driverRepository.findById(Long.valueOf(driverId))
                    .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId)).getUser();
            DriverInfoDTO driverInfoDTO = getDriverInfoDTO(driverId,(Double)result[1], user);
            fuelConsumptionMap.add(driverInfoDTO);
        }
        return fuelConsumptionMap;
    }

    private DriverInfoDTO getDriverInfoDTO(Integer id, Double result, User user) {
        String name = user.getFirstName()+" "+ user.getLastName();
        String classification;
        if ((Double) result <=10){
            classification ="Excellent";
        } else if ((Double) result >10 && (Double) result <=12) {
            classification = "Good";
        }else {
            classification = "Poor";
        }
        return new DriverInfoDTO(id,name, (Double) result,classification);
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

    public   Map<String, Integer> getAverageFuelConsumptionClassificationMonth(Long managerId, int month, int year) {
        Map<String, Integer> classificationCount = new HashMap<>();
        List<Object[]> results  = driverRecordRepository.findAverageFuelConsumptionByDriversAndMonth(managerId,month,year);
        for ( Object[] result : results){
            Integer driverId = (Integer) result[0];
            Double avgFuelConsumption = (Double)result[1];
            String classification;
            if (avgFuelConsumption<=10){
                classification ="Excellent";
            } else if (avgFuelConsumption>10 && avgFuelConsumption <=12) {
                classification = "Good";
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

    public List<ChartLabelsDTO> getDailyGroupedConsumption(Long managerId){
        List<Object[]> results = driverRecordRepository.getDailyConsumption(managerId);
        List<ChartLabelsDTO> FuelConsumptionList = new ArrayList<>();
        for (Object[] result : results) {
            int dayOfYear = ((Number) result[0]).intValue();
            double avgFuelConsumption = (Double) result[1];
            ChartLabelsDTO chartLabel = new ChartLabelsDTO("Day "+ dayOfYear, avgFuelConsumption );
            FuelConsumptionList.add(chartLabel);
        }
        return FuelConsumptionList;
    }

    public List<ChartLabelsDTO> getWeeklyGroupedConsumption(Long managerId) {
        List<Object[]> results = driverRecordRepository.getWeeklyConsumption(managerId);
        List<ChartLabelsDTO> FuelConsumptionList = new ArrayList<>();
        for (Object[] result : results) {
            int weekOfYear = ((Number) result[0]).intValue();
            double avgFuelConsumption = (Double) result[1];
            ChartLabelsDTO chartLabel = new ChartLabelsDTO("Week "+ weekOfYear, avgFuelConsumption);
            FuelConsumptionList.add(chartLabel);
        }
        return FuelConsumptionList;
    }

    public List<ChartLabelsDTO> getMonthlyGroupedConsumption(Long managerId) {
        List<Object[]> results = driverRecordRepository.getMonthlyConsumption(managerId);
        List<ChartLabelsDTO> FuelConsumptionList = new ArrayList<>();
        for (Object[] result : results) {
            String month = ((String) result[0]);
            double avgFuelConsumption = (Double) result[1];
            String formattedMonth = formatPeriodToMonthName(month);
            ChartLabelsDTO chartLabel = new ChartLabelsDTO(formattedMonth, avgFuelConsumption );
            FuelConsumptionList.add(chartLabel);
        }
        return FuelConsumptionList;
    }

    private String formatPeriodToMonthName(String period) {
        try {
            YearMonth yearMonth = YearMonth.parse(period);
            return yearMonth.getMonth()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + yearMonth.getYear(); //FULL for full month name
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid period format: " + period + ". Expected format: YYYY-MM.");
        }}

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

    public List<PredictedFuelConsumptionDTO> getDailyAveragePredictedFuelConsumptionByManagerAndDate(Long managerId, Date startDate, Date endDate) {
        List<Object[]> results = driverRecordRepository.findDailyAveragePredictedFuelConsumptionByManagerAndDate(managerId, startDate, endDate);
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

}
