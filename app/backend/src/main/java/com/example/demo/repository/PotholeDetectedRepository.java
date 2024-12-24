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
            SELECT\s
               DAYNAME(time_met) AS day_name,
               DAYOFWEEK(time_met) AS day_order,
               COUNT(*) AS pothole_detected
           FROM pothole_detected
           WHERE DATE(time_met) BETWEEN\s
                 DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY)
                 AND DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY), INTERVAL 6 DAY)
                 AND user_id = :userId
           GROUP BY day_name, day_order
           ORDER BY day_order;
            """, nativeQuery = true)
    List<Object[]> getInfoPerDay(@Param("userId") String userId);

    @Query(value = """
    SELECT pothole_id, COUNT(*) AS count
    FROM pothole_detected
    WHERE DATE(time_met) BETWEEN\s
         DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY)
         AND DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY), INTERVAL 6 DAY)
         AND user_id = :userId
    GROUP BY pothole_id
    ORDER BY count DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getMostEncounteredPothole(@Param("userId") String userId);

}
