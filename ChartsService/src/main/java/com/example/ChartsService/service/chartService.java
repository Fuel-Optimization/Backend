package com.example.ChartsService.service;

import com.example.ChartsService.dto.AverageContributionsDTO;
import com.example.ChartsService.dto.DriverBehaviorDTO;
import com.example.ChartsService.dto.DriverInfoDTO;
import com.example.ChartsService.dto.PredictedFuelConsumptionDTO;
import com.example.model.Repository.DriverRecordRepository;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.User;
import org.apache.commons.lang3.tuple.Pair;
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

    public List<DriverInfoDTO> getAverageFuelConsumptionByTop5ForManagerNoDate(Long managerId) {
        List<Object[]> results = driverRecordRepository.findAverageFuelConsumptionByTop5ForManagerNoDate(managerId);

//        Map<Integer, Double> fuelConsumptionMap = new LinkedHashMap<>();
        //A map to store the driverId and DriverInfoDTO
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
            classification ="Excellent ";
        } else if ((Double) result >10 && (Double) result <=12) {
            classification = "Good";
        }else {
            classification = "Poor";
        }
        return new DriverInfoDTO(id,name, (Double) result,classification);
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
