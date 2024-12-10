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
}
