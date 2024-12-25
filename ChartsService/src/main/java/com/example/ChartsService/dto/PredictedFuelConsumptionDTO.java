package com.example.ChartsService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PredictedFuelConsumptionDTO {

    @JsonProperty("fuel_consumption")
    private Double predictedFuelConsumption;

    @JsonProperty("time")
    private Date time;
}
