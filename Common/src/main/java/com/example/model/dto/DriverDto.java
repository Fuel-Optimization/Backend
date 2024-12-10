package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class DriverDto {
    private Long id;
    private UserDTO user;
    private int yearsOfExperience;
}
