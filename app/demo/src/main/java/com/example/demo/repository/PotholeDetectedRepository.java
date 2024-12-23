package com.example.demo.repository;

import com.example.demo.entity.PotholeDetected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PotholeDetectedRepository extends JpaRepository<PotholeDetected, String> {
    @Query(value = """
            select count(*) from pothole_detected where user_id = :userId
            """, nativeQuery = true)
    int totalPotholeEncountered(@Param("userId") String userId);

    @Query(value = """
            Select
                DAYNAME(time_met) AS day_name,
                DAYOFWEEK(time_met) AS day_order,
                count(*) as pothole_detected
            From pothole_detected
            Where DATE(time_met) BETWEEN 
                  DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)  
                  AND DATE_ADD(CURDATE(), INTERVAL (6 - WEEKDAY(CURDATE())) DAY) 
                  AND user_id = :userId
              GROUP BY day_name, day_order
              ORDER BY day_order;
            """, nativeQuery = true)
    List<Object[]> getInfoPerDay(@Param("userId") String userId);

    @Query(value = """
    SELECT pothole_id, COUNT(*) AS count
    FROM pothole_detected
    WHERE DATE(time_met) BETWEEN 
          DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)  
          AND DATE_ADD(CURDATE(), INTERVAL (6 - WEEKDAY(CURDATE())) DAY) 
          AND user_id = :userId
    GROUP BY pothole_id
    ORDER BY count DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getMostEncounteredPothole(@Param("userId") String userId);

}
