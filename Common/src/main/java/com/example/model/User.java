package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Family name is required")
    private String familyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "national_id", nullable = false, unique = true)
    @NotBlank(message = "National ID is required")
    private String nationalid;

    @Enumerated(EnumType.STRING) // Stores the enum as a string in the database
    @Column(nullable = false)
    private Role role;

    // Enum for Role
    public enum Role {
        Driver,
        Manager
    }
}
