package com.example.model.Repository;

import com.example.model.model.DriverRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DriverRecordRepository extends JpaRepository<DriverRecord, Long> {
    List<DriverRecord> findByDriverId(Long driverId);
    @Query("SELECT r FROM DriverRecord r WHERE r.driverId = :driverId AND r.time BETWEEN :startDate AND :endDate")
    List<DriverRecord> findRecordsByDriverIdAndDateRange(
            @Param("driverId") Long driverId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query(value = "SELECT f.driver_id, AVG(f.predicted_fuel_consumption) AS avgFuelConsumption " +
            "FROM driver_record f " +
            "JOIN drivers d ON f.driver_id = d.id " +
            "WHERE d.manager_id = :managerId AND f.time BETWEEN :startDate AND :endDate " +
            "GROUP BY f.driver_id " +
            "ORDER BY avgFuelConsumption " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findAverageFuelConsumptionByDriverAndManagerDate(
            @Param("managerId") Long managerId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT f.driverId, AVG(f.predictedFuelConsumption) AS avgFuelConsumption " +
            "FROM DriverRecord f " +
            "JOIN Driver d ON f.driverId = d.id  " +
            "WHERE d.manager.id = :managerId " +
            "GROUP BY f.driverId " +
            "ORDER BY avgFuelConsumption")
    List<Object[]> findAverageFuelConsumptionByDriverAndManager(Long managerId);

    @Query(value = "select AVG(dr.engine_speed_contribution) AS engineSpeedContribution, " +
        "AVG(dr.vehicle_speed_contribution) AS vehicleSpeedContribution, " +
        "AVG(dr.accelerator_pedal_value_contribution) AS acceleratorPedalValueContribution, "+
        "AVG(dr.intake_air_pressure_contribution) AS intakeAirPressureContribution, " +
        "AVG(dr.acceleration_speed_longitudinal_contribution) AS accelerationSpeedLongitudinalContribution, "+
        "AVG(dr.minimum_indicated_engine_torque_contribution) AS minimumIndicatedEngineTorqueContribution, " +
        "AVG(dr.indication_of_brake_switch_on_off_contribution) AS indicationOfBrakeSwitchOnOffContribution, " +
        "AVG(dr.converter_clutch_contribution) AS converterClutchContribution, " +
        "AVG(dr.engine_idle_target_speed_contribution) AS engineIdleTargetSpeedContribution, "+
        "AVG(dr.current_spark_timing_contribution) AS currentSparkTimingContribution, " +
        "AVG(dr.master_cylinder_pressure_contribution) AS masterCylinderPressureContribution, "+
        "AVG(dr.torque_of_friction_contribution) AS torqueOfFrictionContribution, " +
        "AVG(dr.engine_in_fuel_cut_off_contribution) AS engineInFuelCutOffContribution, "+
        "AVG(dr.current_gear_contribution) AS currentGearContribution, " +
        "AVG(dr.calculated_road_gradient_contribution) AS calculatedRoadGradientContribution, " +
        "AVG(dr.long_term_fuel_trim_bank1contribution) AS longTermFuelTrimBank1Contribution "+
        "FROM driver_record dr where dr.driver_id = :driverId AND dr.time BETWEEN :startDate AND :endDate ", nativeQuery = true)
    List<Object[]> findAverageAttributesContributionsByDriverAndDate(
            @Param("driverId") Long driverId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT dr.predictedFuelConsumption," +
            "dr.time " +
            "FROM DriverRecord dr " +
            "where dr.driverId = :driverId "+
            "AND dr.time BETWEEN :startDate AND :endDate "+
            "ORDER BY FUNCTION('DATE', dr.time) ")
    List<Object[]> findPredictedFuelConsumptionByDriverAndDate(
            @Param("driverId") Long driverId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT AVG(dr.predictedFuelConsumption) AS dailyAverage," +
            "FUNCTION('DATE', dr.time) AS day " +
            "FROM DriverRecord dr " +
            "where dr.driverId = :driverId "+
            "AND dr.time BETWEEN :startDate AND :endDate "+
            "GROUP BY FUNCTION('DATE', dr.time) " +
            "ORDER BY FUNCTION('DATE', dr.time) ")
    List<Object[]> findDailyAveragePredictedFuelConsumptionByDriverAndDate(
            @Param("driverId") Long driverId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}
