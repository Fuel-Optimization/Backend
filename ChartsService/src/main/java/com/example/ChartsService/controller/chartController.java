package com.example.ChartsService.controller;

import com.example.ChartsService.dto.AverageContributionsDTO;
import com.example.ChartsService.dto.DriverBehaviorDTO;
import com.example.ChartsService.dto.DriverInfoDTO;
import com.example.ChartsService.dto.PredictedFuelConsumptionDTO;
import com.example.ChartsService.service.chartService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @GetMapping("/top5drivers")
    public List<DriverInfoDTO> getTop5driversNoDate(@RequestParam Long managerId){
        return chartService.getAverageFuelConsumptionByTop5ForManagerNoDate(managerId);
    }

    @GetMapping("/average-fuel-consumption-top5-manager")
    public Map<Integer, Double> getAverageFuelConsumptionManagerDate(
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

    @GetMapping("/average-fuel-consumption-classification")
    public  Map<String, Integer> getAverageFuelConsumptionManagerClassification(
            @RequestParam Long managerId,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String startDate,
            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String endDate) throws ParseException {
        Date start = null;
        Date end = null;

        Pair<Date,Date> datePair =chartService.setDefaultDates(startDate,endDate);
        start = datePair.getKey();
        end = datePair.getValue();

        System.out.println(start+" "+end);
        return chartService.getAverageFuelConsumptionClassificationByManagers(managerId, start, end);
    }

    @GetMapping("/grouped")
    public Map<String, Object> getGroupedConsumption() {
        return chartService.getGroupedConsumption();
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

    @GetMapping("/average-daily-predicted-fuel-consumption-manager-date/{managerId}")
    public List<PredictedFuelConsumptionDTO> getDailyAveragePredictedFuelConsumptionByManagerAndDate(
            @PathVariable("managerId") Long managerId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) throws ParseException {
        // Convert String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        System.out.println(start +" "+ end);

        return chartService.getDailyAveragePredictedFuelConsumptionByManagerAndDate(managerId,start,end);
    }
}
