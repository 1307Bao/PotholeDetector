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
            SELECT\s
                 DAYNAME(time_detected) AS day_name,
                 DAYOFWEEK(time_detected) AS day_order,
                 COUNT(*) AS pothole_detected
             FROM pothole
             WHERE DATE(time_detected) BETWEEN\s
                   DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY)
                   AND DATE_ADD(DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY), INTERVAL 6 DAY)
                   AND user_id = :userId
             GROUP BY day_name, day_order
             ORDER BY day_order;
            """, nativeQuery = true)
    List<Object[]> getInfoPerDay(@Param("userId") String userId);
}
