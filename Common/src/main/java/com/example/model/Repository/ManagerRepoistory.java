package com.example.model.Repository;

import com.example.model.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepoistory extends JpaRepository<Manager, Long> {
    Optional<Manager> findById(Long id);
    Manager findByUserId(Long userId);
}
