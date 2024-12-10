package com.example.model.Repository;

import com.example.model.model.Driver;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
     Optional<Driver> findById(Long id);

     @Query("SELECT d FROM Driver d JOIN d.user u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name% OR u.familyName LIKE %:name%")
     List<Driver> findByName(@Param("name") String name);

     @Query("SELECT d FROM Driver d JOIN d.user u WHERE u.nationalid = :nationalId")
     List<Driver> findByNationalId(@Param("nationalId") String nationalId);


}
