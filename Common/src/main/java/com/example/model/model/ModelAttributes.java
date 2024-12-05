package com.example.model.model;

import java.util.HashMap;
import java.util.Map;

public class ModelAttributes {


        private int engineSpeed;
        private int vehicleSpeed;
        private double acceleratorPedalValue;
        private int intakeAirPressure;
        private double accelerationSpeedLongitudinal;
        private int minimumIndicatedEngineTorque;
        private int indicationOfBrakeSwitchOnOff;
        private int converterClutch;
        private int engineIdleTargetSpeed;
        private int currentSparkTiming;
        private double masterCylinderPressure;
        private int torqueOfFriction;
        private int engineInFuelCutOff;
        private int currentGear;
        private double calculatedRoadGradient;
        private double longTermFuelTrimBank1;

        public int getEngineSpeed() {
            return engineSpeed;
        }

        public void setEngineSpeed(int engineSpeed) {
            this.engineSpeed = engineSpeed;
        }

        public int getVehicleSpeed() {
            return vehicleSpeed;
        }

        public void setVehicleSpeed(int vehicleSpeed) {
            this.vehicleSpeed = vehicleSpeed;
        }

        public double getAcceleratorPedalValue() {
            return acceleratorPedalValue;
        }

        public void setAcceleratorPedalValue(double acceleratorPedalValue) {
            this.acceleratorPedalValue = acceleratorPedalValue;
        }

        public int getIndicationOfBrakeSwitchOnOff() {
            return indicationOfBrakeSwitchOnOff;
        }

        public void setIndicationOfBrakeSwitchOnOff(int indicationOfBrakeSwitchOnOff) {
            this.indicationOfBrakeSwitchOnOff = indicationOfBrakeSwitchOnOff;
        }

        public int getCurrentGear() {
            return currentGear;
        }

        public void setCurrentGear(int currentGear) {
            this.currentGear = currentGear;
        }

        public double getLongTermFuelTrimBank1() {
            return longTermFuelTrimBank1;
        }

        public void setLongTermFuelTrimBank1(double longTermFuelTrimBank1) {
            this.longTermFuelTrimBank1 = longTermFuelTrimBank1;
        }

        public int getIntakeAirPressure() {
            return intakeAirPressure;
        }

        public void setIntakeAirPressure(int intakeAirPressure) {
            this.intakeAirPressure = intakeAirPressure;
        }

        public int getEngineInFuelCutOff() {
            return engineInFuelCutOff;
        }

        public void setEngineInFuelCutOff(int engineInFuelCutOff) {
            this.engineInFuelCutOff = engineInFuelCutOff;
        }

        public int getTorqueOfFriction() {
            return torqueOfFriction;
        }

        public void setTorqueOfFriction(int torqueOfFriction) {
            this.torqueOfFriction = torqueOfFriction;
        }

        public double getMasterCylinderPressure() {
            return masterCylinderPressure;
        }

        public void setMasterCylinderPressure(double masterCylinderPressure) {
            this.masterCylinderPressure = masterCylinderPressure;
        }

        public int getEngineIdleTargetSpeed() {
            return engineIdleTargetSpeed;
        }

        public void setEngineIdleTargetSpeed(int engineIdleTargetSpeed) {
            this.engineIdleTargetSpeed = engineIdleTargetSpeed;
        }

        public int getConverterClutch() {
            return converterClutch;
        }

        public void setConverterClutch(int converterClutch) {
            this.converterClutch = converterClutch;
        }

        public double getAccelerationSpeedLongitudinal() {
            return accelerationSpeedLongitudinal;
        }

        public void setAccelerationSpeedLongitudinal(double accelerationSpeedLongitudinal) {
            this.accelerationSpeedLongitudinal = accelerationSpeedLongitudinal;
        }

        public int getMinimumIndicatedEngineTorque() {
            return minimumIndicatedEngineTorque;
        }

        public void setMinimumIndicatedEngineTorque(int minimumIndicatedEngineTorque) {
            this.minimumIndicatedEngineTorque = minimumIndicatedEngineTorque;
        }

        public int getCurrentSparkTiming() {
            return currentSparkTiming;
        }

        public void setCurrentSparkTiming(int currentSparkTiming) {
            this.currentSparkTiming = currentSparkTiming;
        }

        public double getCalculatedRoadGradient() {
            return calculatedRoadGradient;
        }

        public void setCalculatedRoadGradient(double calculatedRoadGradient) {
            this.calculatedRoadGradient = calculatedRoadGradient;
        }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("Engine_speed", engineSpeed);
        data.put("Vehicle_speed", vehicleSpeed);
        data.put("Accelerator_Pedal_value", acceleratorPedalValue);
        data.put("Intake_air_pressure", intakeAirPressure);
        data.put("Acceleration_speed_-_Longitudinal", accelerationSpeedLongitudinal);
        data.put("Minimum_indicated_engine_torque", minimumIndicatedEngineTorque);
        data.put("Indication_of_brake_switch_ON/OFF", indicationOfBrakeSwitchOnOff);
        data.put("Converter_clutch", converterClutch);
        data.put("Engine_Idel_Target_Speed", engineIdleTargetSpeed);
        data.put("Current_spark_timing", currentSparkTiming);
        data.put("Master_cylinder_pressure", masterCylinderPressure);
        data.put("Torque_of_friction", torqueOfFriction);
        data.put("Engine_in_fuel_cut_off", engineInFuelCutOff);
        data.put("Current_Gear", currentGear);
        data.put("Calculated_road_gradient", calculatedRoadGradient);
        data.put("Long_Term_Fuel_Trim_Bank1", longTermFuelTrimBank1);
        return data;
    }


}
