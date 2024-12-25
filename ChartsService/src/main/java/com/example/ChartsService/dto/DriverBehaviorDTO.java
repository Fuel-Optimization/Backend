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
public class DriverBehaviorDTO {

    @JsonProperty("driver_behavior")
    private String driverBehavior;

    @JsonProperty("time")
    private Date time;
}
