package com.example.ChartsService.controller;

import com.example.ChartsService.dto.*;
import com.example.ChartsService.service.chartService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/charts")
public class chartController {
    private chartService chartService;

    public chartController(com.example.ChartsService.service.chartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/overAllAvgFuelConsumption/{managerId}")
    public Double getOverAllAverageFuelConsumption(@PathVariable("managerId") Long managerId){
        return chartService.getOverAllAverageFuelConsumption(managerId);
    }

    @GetMapping("/monthlyOverAllAvgFuelConsumption/{managerId}")
    public Double getMonthlyAverageFuelConsumption(@PathVariable("managerId") Long managerId){
        int defaultYear = 2024;
        return chartService.getMonthlyAverageFuelConsumption(managerId,defaultYear);
    }

    @GetMapping("/top5drivers")
    public List<DriverInfoDTO> getTop5driversNoDate(@RequestParam Long managerId){
        return chartService.getAverageFuelConsumptionByTop5ForManagerNoDate(managerId);
    }

    @GetMapping("/top5driversDate")
    public List<DriverInfoDTO> getAverageFuelConsumptionManagerDate(
            @RequestParam Long managerId,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {

        Date start = null;
        Date end = null;

        Pair<Date,Date> datePair =chartService.setDefaultDates(startDate,endDate);
        start = datePair.getKey();
        end = datePair.getValue();

        System.out.println(start+" "+end);

        return chartService.getAverageFuelConsumptionByTop5ForManager(managerId,start,end);
    }

    @GetMapping("/average-fuel-consumption-driver")
    public  Map<String, Double> getAverageFuelConsumptionManager(
            @RequestParam Long managerId,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {

        Date start = null;
        Date end = null;

        Pair<Date,Date> datePair =chartService.setDefaultDates(startDate,endDate);
        start = datePair.getKey();
        end = datePair.getValue();

        System.out.println(start+" "+end);

        return chartService.getAverageFuelConsumptionByManagers(managerId, start, end);
    }

    @GetMapping("/average-fuel-consumption-classification-month")
    public  Map<String, Integer> getAverageFuelConsumptionManagerClassification(
            @RequestParam("managerId") Long managerId,
            @RequestParam int month,
            @RequestParam(required = false, defaultValue = "2024") int year){

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month. Month must be between 1 and 12.");
        }

        return chartService.getAverageFuelConsumptionClassificationMonth(managerId, month, year);
    }


    @GetMapping("/dailyGrouped/{managerId}")
    public List<ChartLabelsDTO> getDailyGroupedConsumption(@PathVariable("managerId") Long managerId) {
        return chartService.getDailyGroupedConsumption(managerId);
    }

    @GetMapping("/weeklyGrouped/{managerId}")
    public List<ChartLabelsDTO> getWeeklyGroupedConsumption(@PathVariable("managerId") Long managerId) {
        return chartService.getWeeklyGroupedConsumption(managerId);
    }

    @GetMapping("/monthlyGrouped/{managerId}")
    public List<ChartLabelsDTO> getMonthlyGroupedConsumption(@PathVariable("managerId") Long managerId) {
        return chartService.getMonthlyGroupedConsumption(managerId);
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
