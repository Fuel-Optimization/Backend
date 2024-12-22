package com.example.model.service;

import com.example.model.Repository.AlertRepository;
import com.example.model.model.Alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    // Method to get all alerts for a specific manager ID
    public List<Alert> getAlertsByManagerId(Long managerId) {
        return alertRepository.findAllByManagerIdOrderByCreatedAtDesc(managerId);
    }

    // Method to get the last 5 alerts for a specific driver ID
    public List<Alert> getLast5AlertsByDriverId(Long driverId) {
        return alertRepository.findTop5ByDriverIdOrderByCreatedAtDesc(driverId);
    }
}
