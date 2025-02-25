package com.backend.urbia.repository;

import com.backend.urbia.model.Reportes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportesRepository extends JpaRepository<Reportes, Long> {

    @Query(value = "SELECT * FROM reportes r " +
            "WHERE ST_DWithin(r.ubicacion, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326), :distance) " +
            "AND r.fecha_creacion >= :fechaThreshold", nativeQuery = true)
    List<Reportes> findReportsWithinDistance(double lng, double lat, double distance, LocalDateTime fechaThreshold);
}
