package com.example.model.controller;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.Driver;
import com.example.model.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;
    private final DriverRepository driverRepository;

    public DriverController(DriverService driverService, DriverRepository driverRepository) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/{managerID}")
    public List<Map<String, Object>> getDriversByManagerID(@PathVariable Long managerID) {
        List<Driver> drivers = driverRepository.findByManagerId(managerID);
        return driverService.getDriverDetails(drivers);
    }

    @GetMapping("/driver/{driverID}")
    public Map<String, Object> getDriverByDriverID(@PathVariable Long driverID) {
        Driver driver = driverRepository.findById(driverID)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverID));
        return driverService.getDriverDetailsForOneDriver(driver);
    }

    @GetMapping("/averages")
    public ResponseEntity<List<Map<String, Object>>> getDriverCombinedAverages(
            @RequestParam Long driverId
    ) {
        return ResponseEntity.ok(driverService.getDriverCombinedAverages(driverId));
    }

    @GetMapping("/summary")
    public Map<String, Object> getDriverSummary(@RequestParam Long driverId) {
        return driverService.getDriverSummary(driverId);
    }

    @GetMapping("/attributes")
    public ResponseEntity<Map<String, Object>> getAttributeData(
            @RequestParam Long driverId,
            @RequestParam String attribute) {
        Map<String, Object> data = driverService.getAttributeData(driverId, attribute);
        return ResponseEntity.ok(data);
    }

}




























