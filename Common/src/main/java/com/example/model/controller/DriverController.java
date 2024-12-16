package com.example.model.controller;
import com.example.model.Repository.DriverRepository;
import com.example.model.model.Driver;
import com.example.model.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public List<Map<String, Object>> getDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return driverService.getDriverDetails(drivers);
    }

    @GetMapping("/averages")
    public ResponseEntity<List<Map<String, Object>>> getCombinedAverages(
            @RequestParam Long driverId
    ) {
        return ResponseEntity.ok(driverService.getCombinedAverages(driverId));
    }

    @GetMapping("/summary")
    public Map<String, Object> getDriverSummary(@RequestParam Long driverId) {
        return driverService.getDriverSummary(driverId);
    }
}




























