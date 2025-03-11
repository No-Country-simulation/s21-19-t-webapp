package com.nocountry.urbia.repository;

import com.nocountry.urbia.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    // Método para obtener todos los mensajes asociados a un reporte específico
    List<Mensaje> findByReporteId(Long reporteId);
}
