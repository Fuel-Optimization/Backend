package com.example.ChartsService.dto;

import lombok.*;

@Setter
@Getter
@Data
public class DriverInfoDTO {
        private Integer id;
        private String name;
        private Double avgFuelConsumption;
        private String classification;

        // Constructor
        public DriverInfoDTO(Integer id,String name, Double avgFuelConsumption, String classification) {
            this.id=id;
            this.name = name;
            this.avgFuelConsumption = avgFuelConsumption;
            this.classification = classification;
        }


}
