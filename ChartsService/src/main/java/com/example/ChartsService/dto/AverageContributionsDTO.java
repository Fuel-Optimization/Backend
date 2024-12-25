package com.example.ChartsService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AverageContributionsDTO {

    @JsonProperty("engine_speed_contribution")
    private Double engineSpeedContribution;

    @JsonProperty("vehicle_speed_contribution")
    private Double vehicleSpeedContribution;

    @JsonProperty("accelerator_pedal_value_contribution")
    private Double acceleratorPedalValueContribution;

    @JsonProperty("intake_air_pressure_contribution")
    private Double intakeAirPressureContribution;

    @JsonProperty("acceleration_speed_longitudinal_contribution")
    private Double accelerationSpeedLongitudinalContribution;

    @JsonProperty("minimum_indicated_engine_torque_contribution")
    private Double minimumIndicatedEngineTorqueContribution;

    @JsonProperty("indication_of_brake_switch_on_off_contribution")
    private Double indicationOfBrakeSwitchOnOffContribution;

    @JsonProperty("converter_clutch_contribution")
    private Double converterClutchContribution;

    @JsonProperty("engine_idle_target_speed_contribution")
    private Double engineIdleTargetSpeedContribution;

    @JsonProperty("current_spark_timing_contribution")
    private Double currentSparkTimingContribution;

    @JsonProperty("master_cylinder_pressure_contribution")
    private Double masterCylinderPressureContribution;

    @JsonProperty("torque_of_friction_contribution")
    private Double torqueOfFrictionContribution;

    @JsonProperty("engine_in_fuel_cut_off_contribution")
    private Double engineInFuelCutOffContribution;

    @JsonProperty("current_gear_contribution")
    private Double currentGearContribution;

    @JsonProperty("calculated_road_gradient_contribution")
    private Double calculatedRoadGradientContribution;

    @JsonProperty("long_term_fuel_trim_bank1contribution")
    private Double longTermFuelTrimBank1Contribution;
}
