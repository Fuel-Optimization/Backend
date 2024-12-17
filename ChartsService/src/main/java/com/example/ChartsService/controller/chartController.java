package com.example.ChartsService.controller;

import com.example.ChartsService.service.chartService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
public class chartController {
    private chartService chartService;

    public chartController(com.example.ChartsService.service.chartService chartService) {
        this.chartService = chartService;
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
}
