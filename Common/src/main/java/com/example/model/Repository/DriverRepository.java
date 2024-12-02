package com.example.model.Repository;

import com.example.model.Driver;
import com.example.model.Manager;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
     Optional<Driver> findById(Long id);

}
