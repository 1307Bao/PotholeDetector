package com.example.demo.repository;

import com.example.demo.entity.Pothole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotholeRepository extends JpaRepository<Pothole, String> {
    boolean existsByLongitudeAndLatitude(double longitude, double latitude);

    @Query(value = """
            select count(*) from pothole where user_id = :userId
            """, nativeQuery = true)
    int totalPotholeDetected(@Param("userId") String userId);

    @Query(value = """
            select count(*) from pothole where user_id = :userId and date(time_detected) = curdate()
            """, nativeQuery = true)
    int todayPotholeDetected(@Param("userId") String userId);

    @Query(value = """
            select * from pothole where user_id = :userId order by time_detected desc limit 6
            """, nativeQuery = true)
    List<Pothole> getCurrentPothole(@Param("userId") String userId);

    @Query(value = """
            Select
                DAYNAME(time_detected) AS day_name,
                DAYOFWEEK(time_detected) AS day_order,
                count(*) as pothole_detected
            From pothole
            Where DATE(time_detected) BETWEEN 
                  DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)  
                  AND DATE_ADD(CURDATE(), INTERVAL (6 - WEEKDAY(CURDATE())) DAY) 
                  AND user_id = :userId
              GROUP BY day_name, day_order
              ORDER BY day_order;
            """, nativeQuery = true)
    List<Object[]> getInfoPerDay(@Param("userId") String userId);
}
