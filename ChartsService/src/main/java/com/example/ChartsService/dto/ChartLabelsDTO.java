package com.example.ChartsService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class ChartLabelsDTO {
    private String label;
    private Double avgFuelConsumption;

    public ChartLabelsDTO(String label, Double avgFuelConsumption) {
        this.label = label;
        this.avgFuelConsumption = avgFuelConsumption;
    }
}
