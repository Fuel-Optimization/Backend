package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    @Column(unique = true, nullable = false)
    private String licensePlate;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Manufacturer is required")
    @Column(nullable = false)
    private String manufacturer;

    private Integer year;

    @OneToOne(optional = true)
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = true)
    private Driver driver;
}
