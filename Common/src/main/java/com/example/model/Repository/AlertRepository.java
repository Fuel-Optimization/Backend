package com.example.model.Repository;

import com.example.model.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByManagerIdOrderByCreatedAtDesc(Long managerId);

    @Query("SELECT a FROM Alert a WHERE a.driverId = :driverId ORDER BY a.createdAt DESC LIMIT 5")
    List<Alert> findTop5ByDriverIdOrderByCreatedAtDesc(@Param("driverId") Long driverId);
}
