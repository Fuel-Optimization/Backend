package com.example.model.service;

import com.example.model.Repository.ManagerRepoistory;
import com.example.model.model.Manager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ManagerService {
    private static final Logger logger = Logger.getLogger(ManagerService.class.getName());
    private final ManagerRepoistory managerRepository;

    public ManagerService(ManagerRepoistory managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Manager FindbyUserId(Long id) {
        try {
            Manager manager = managerRepository.findByUserId(id);
            if (manager == null) {
                logger.warning("No Manager found for user ID: " + id);
            }
            return manager;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while finding Manager by user ID: " + id, e);
            throw new RuntimeException("Could not fetch Manager by user ID: " + id, e);
        }
    }
}
