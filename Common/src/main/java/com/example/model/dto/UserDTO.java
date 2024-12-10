package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String familyName;
    private String email;
    private String phoneNumber;
    private String nationalid;
//    private String role;
}
