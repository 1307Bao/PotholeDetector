package com.example.demo.repository;

import com.example.demo.entity.PotholePotential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PotholePotentialRepository extends JpaRepository<PotholePotential, String> {
    List<PotholePotential> findByUserId(String userId);

    @Query(value = """
            select count(*) from pothole where user_id = :userId
            """, nativeQuery = true)
    int totalPotholeEncountered(@Param("userId") String userId);
}
