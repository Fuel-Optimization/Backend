package com.example.model.Repository;

import com.example.model.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByManagerIdOrderByCreatedAtDesc(Long managerId);
}
