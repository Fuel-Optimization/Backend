package com.example.ChartsService.controller;

import com.example.ChartsService.dto.AverageContributionsDTO;
import com.example.ChartsService.dto.DriverBehaviorDTO;
import com.example.ChartsService.dto.PredictedFuelConsumptionDTO;
import com.example.ChartsService.service.chartService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class chartController {
    private chartService chartService;

    public chartController(com.example.ChartsService.service.chartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/average-fuel-consumption-manager-date")
    public Map<Integer, Double> getAverageFuelConsumptionManagerDate(
            @RequestParam Long managerId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {

        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getAverageFuelConsumptionByManagersDate(managerId,start,end);
    }

    @GetMapping("/average-fuel-consumption-classification")
    public  Map<Integer, Pair<Double, String>> getAverageFuelConsumptionManagerClassification(
            @RequestParam Long managerId){
        return chartService.getAverageFuelConsumptionByManagers(managerId);
    }

    @GetMapping("/average-attributes-contributions-driver-date/{driverId}")
    public AverageContributionsDTO getAverageAttributesContributionsByDriverAndDate(
            @PathVariable("driverId") Long driverID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getAverageAttributesContributionsByDriverAndDate(driverID,start,end);
    }

    @GetMapping("/predicted-fuel-consumption-driver-date/{driverId}")
    public List<PredictedFuelConsumptionDTO> getPredictedFuelConsumptionByDriverAndDate(
            @PathVariable("driverId") Long driverID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getPredictedFuelConsumptionByDriverAndDate(driverID,start,end);
    }

    @GetMapping("/average-daily-predicted-fuel-consumption-driver-date/{driverId}")
    public List<PredictedFuelConsumptionDTO> getDailyAveragePredictedFuelConsumptionByDriverAndDate(
            @PathVariable("driverId") Long driverID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getDailyAveragePredictedFuelConsumptionByDriverAndDate(driverID,start,end);
    }

    @GetMapping("/driver-behavior/{driverId}")
    public List<DriverBehaviorDTO> getDriverBehavior(
            @PathVariable("driverId") Long driverID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getDriverBehavior(driverID,start,end);
    }

    @GetMapping("/daily-driver-behavior/{driverId}")
    public List<DriverBehaviorDTO> getDailyDriverBehavior(
            @PathVariable("driverId") Long driverID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getDailyDriverBehavior(driverID,start,end);
    }
}
