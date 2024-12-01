package com.example.mappers;

import com.example.model.ModelAttributes;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModelAttributesMapper {

    public ModelAttributes map(Map<String, Object> data) {
        ModelAttributes modelAttributes = new ModelAttributes();
        modelAttributes.setEngineSpeed(convertToInt(data.get("Engine_speed")));
        modelAttributes.setVehicleSpeed(convertToInt(data.get("Vehicle_speed")));
        modelAttributes.setAcceleratorPedalValue(convertToDouble(data.get("Accelerator_Pedal_value")));
        modelAttributes.setIntakeAirPressure(convertToInt(data.get("Intake_air_pressure")));
        modelAttributes.setAccelerationSpeedLongitudinal(convertToDouble(data.get("Acceleration_speed_-_Longitudinal")));
        modelAttributes.setMinimumIndicatedEngineTorque(convertToInt(data.get("Minimum_indicated_engine_torque")));
        modelAttributes.setIndicationOfBrakeSwitchOnOff(convertToInt(data.get("Indication_of_brake_switch_ON/OFF")));
        modelAttributes.setConverterClutch(convertToInt(data.get("Converter_clutch")));
        modelAttributes.setEngineIdleTargetSpeed(convertToInt(data.get("Engine_Idel_Target_Speed")));
        modelAttributes.setCurrentSparkTiming(convertToInt(data.get("Current_spark_timing")));
        modelAttributes.setMasterCylinderPressure(convertToDouble(data.get("Master_cylinder_pressure")));
        modelAttributes.setTorqueOfFriction(convertToInt(data.get("Torque_of_friction")));
        modelAttributes.setEngineInFuelCutOff(convertToInt(data.get("Engine_in_fuel_cut_off")));
        modelAttributes.setCurrentGear(convertToInt(data.get("Current_Gear")));
        modelAttributes.setCalculatedRoadGradient(convertToDouble(data.get("Calculated_road_gradient")));
        modelAttributes.setLongTermFuelTrimBank1(convertToDouble(data.get("Long_Term_Fuel_Trim_Bank1")));
        return modelAttributes;
    }

    private int convertToInt(Object value) {
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            throw new IllegalArgumentException("Invalid value for Integer: " + value);
        }
    }

    private double convertToDouble(Object value) {
        if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException("Invalid value for Double: " + value);
        }
    }
}
