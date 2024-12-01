package com.example.model.Repository;

import com.example.model.DriverRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRecordRepository extends JpaRepository<DriverRecord, Long> {
}
