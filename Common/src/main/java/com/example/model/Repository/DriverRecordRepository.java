package com.example.model.Repository;

import com.example.model.model.DriverRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            "WHERE d.manager.Id = :managerId " +
            "GROUP BY f.driverId " +
            "ORDER BY avgFuelConsumption")
    List<Object[]> findAverageFuelConsumptionByDriverAndManager(Long managerId);


}
