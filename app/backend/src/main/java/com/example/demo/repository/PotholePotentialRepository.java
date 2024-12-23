package com.example.demo.repository;

import com.example.demo.entity.PotholePotential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PotholePotentialRepository extends JpaRepository<PotholePotential, String> {
    List<PotholePotential> findByUserId(String userId);


    @Query(value = """
            select count(*) > 1 from pothole_potential
            where user_id = :id and longitude = :longitude and latitude = :latitude
            """, nativeQuery = true)
    boolean isExists(@Param("id") String id,
                     @Param("longitude") double longitude,
                     @Param("latitude") double latitude);
}
