package com.example.mappers;

import com.example.model.DriverRecord;
import com.example.model.ModelAttributes;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Service
public class DriverRecordMapper {

    public DriverRecord map(Map<String, Object> data, ModelAttributes modelAttributes, Map<String, Object> predictionResponse) {
        DriverRecord driverRecord = new DriverRecord();

        // Map attributes from ModelAttributes
        driverRecord.setEngineSpeed(modelAttributes.getEngineSpeed());
        driverRecord.setVehicleSpeed(modelAttributes.getVehicleSpeed());
        driverRecord.setAcceleratorPedalValue(modelAttributes.getAcceleratorPedalValue());
        driverRecord.setIntakeAirPressure(modelAttributes.getIntakeAirPressure());
        driverRecord.setAccelerationSpeedLongitudinal(modelAttributes.getAccelerationSpeedLongitudinal());
        driverRecord.setMinimumIndicatedEngineTorque(modelAttributes.getMinimumIndicatedEngineTorque());
        driverRecord.setIndicationOfBrakeSwitchOnOff(modelAttributes.getIndicationOfBrakeSwitchOnOff());
        driverRecord.setConverterClutch(modelAttributes.getConverterClutch());
        driverRecord.setEngineIdleTargetSpeed(modelAttributes.getEngineIdleTargetSpeed());
        driverRecord.setCurrentSparkTiming(modelAttributes.getCurrentSparkTiming());
        driverRecord.setMasterCylinderPressure(modelAttributes.getMasterCylinderPressure());
        driverRecord.setTorqueOfFriction(modelAttributes.getTorqueOfFriction());
        driverRecord.setEngineInFuelCutOff(modelAttributes.getEngineInFuelCutOff());
        driverRecord.setCurrentGear(modelAttributes.getCurrentGear());
        driverRecord.setCalculatedRoadGradient(modelAttributes.getCalculatedRoadGradient());
        driverRecord.setLongTermFuelTrimBank1(modelAttributes.getLongTermFuelTrimBank1());

        // Map prediction response
        driverRecord.setPredictedFuelConsumption((double) predictionResponse.get("predicted_fuel_consumption"));
        mapFeatureContributions(driverRecord, predictionResponse);

        // Set metadata
        driverRecord.setCarId((String) data.get("Car_ID"));
        driverRecord.setDriverId(parseInt((String) data.get("Driver_ID")));
        setTime(driverRecord, data);

        return driverRecord;
    }

    private void mapFeatureContributions(DriverRecord driverRecord, Map<String, Object> predictionResponse) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> featureContributions = (List<Map<String, Object>>) predictionResponse.get("feature_contributions");
        for (Map<String, Object> feature : featureContributions) {
            String featureName = (String) feature.get("feature");
            double contributionPercentage = (double) feature.get("contribution_percentage");

            switch (featureName) {
                case "Engine_speed":
                    driverRecord.setEngineSpeedContribution(contributionPercentage);
                    break;
                case "Vehicle_speed":
                    driverRecord.setVehicleSpeedContribution(contributionPercentage);
                    break;
                case "Accelerator_Pedal_value":
                    driverRecord.setAcceleratorPedalValueContribution(contributionPercentage);
                    break;
                case "Intake_air_pressure":
                    driverRecord.setIntakeAirPressureContribution(contributionPercentage);
                    break;
                case "Acceleration_speed_-_Longitudinal":
                    driverRecord.setAccelerationSpeedLongitudinalContribution(contributionPercentage);
                    break;
                case "Minimum_indicated_engine_torque":
                    driverRecord.setMinimumIndicatedEngineTorqueContribution(contributionPercentage);
                    break;
                case "Indication_of_brake_switch_ON/OFF":
                    driverRecord.setIndicationOfBrakeSwitchOnOffContribution(contributionPercentage);
                    break;
                case "Converter_clutch":
                    driverRecord.setConverterClutchContribution(contributionPercentage);
                    break;
                case "Engine_Idel_Target_Speed":
                    driverRecord.setEngineIdleTargetSpeedContribution(contributionPercentage);
                    break;
                case "Current_spark_timing":
                    driverRecord.setCurrentSparkTimingContribution(contributionPercentage);
                    break;
                case "Master_cylinder_pressure":
                    driverRecord.setMasterCylinderPressureContribution(contributionPercentage);
                    break;
                case "Torque_of_friction":
                    driverRecord.setTorqueOfFrictionContribution(contributionPercentage);
                    break;
                case "Engine_in_fuel_cut_off":
                    driverRecord.setEngineInFuelCutOffContribution(contributionPercentage);
                    break;
                case "Current_Gear":
                    driverRecord.setCurrentGearContribution(contributionPercentage);
                    break;
                case "Calculated_road_gradient":
                    driverRecord.setCalculatedRoadGradientContribution(contributionPercentage);
                    break;
                case "Long_Term_Fuel_Trim_Bank1":
                    driverRecord.setLongTermFuelTrimBank1Contribution(contributionPercentage);
                    break;
                default:
                    System.err.println("Unknown feature: " + featureName);
            }
        }
    }

    private void setTime(DriverRecord driverRecord, Map<String, Object> data) {
        try {
            String dateString = (String) data.get("Date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            driverRecord.setTime(dateFormat.parse(dateString));
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
    }
}
