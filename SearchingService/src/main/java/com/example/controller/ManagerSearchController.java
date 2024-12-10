package com.example.controller;


import com.example.model.dto.DriverDto;
import com.example.model.model.Driver;
import com.example.model.service.DriverService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ManagerSearchController {
    private final DriverService driverService;

    public ManagerSearchController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/search/by-name")
    public List<DriverDto> searchByName(@RequestParam String name) {
        return driverService.searchByName(name);
    }

    @GetMapping("/search/by-national-id")
    public List<DriverDto> searchByNationalId(@RequestParam String nationalId) {
        return driverService.searchByNationalId(nationalId);
    }
}
