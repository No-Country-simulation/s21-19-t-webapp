package com.nocountry.urbia.repository;

import com.nocountry.urbia.model.Reaccion;
import com.nocountry.urbia.model.Reporte;
import com.nocountry.urbia.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReaccionRepository extends JpaRepository<Reaccion, Long> {
    Optional<Reaccion> findByReporteAndUsuario(Reporte reporte, Usuarios usuario);
    List<Reaccion> findByReporte(Reporte reporte);
}
