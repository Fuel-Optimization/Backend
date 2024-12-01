package com.example.service;

import com.example.model.DriverRecord;


import com.example.model.Repository.DriverRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverRecordService {

    @Autowired
    private DriverRecordRepository driverRecordRepository;

    public void saveDriverRecord(DriverRecord driverRecord) {
        driverRecordRepository.save(driverRecord);
    }
}

