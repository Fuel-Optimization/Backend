package com.example.model.controller;


import com.example.model.model.Alert;

import com.example.model.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Alert>> getAlertsByManagerId(@PathVariable Long managerId) {
        List<Alert> alerts = alertService.getAlertsByManagerId(managerId);
        if (alerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Alert>> getAlertsByDriverId(@PathVariable Long driverId) {
        List<Alert> alerts = alertService.getAlertsByDriverId(driverId);
        if (alerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/driver/{driverId}/last5")
    public ResponseEntity<List<Alert>> getLast5AlertsByDriverId(@PathVariable Long driverId) {
        List<Alert> alerts = alertService.getLast5AlertsByDriverId(driverId);
        if (alerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alerts);
    }
}
