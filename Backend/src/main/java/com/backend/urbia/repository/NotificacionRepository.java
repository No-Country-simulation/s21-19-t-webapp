package com.backend.urbia.repository;

import com.backend.urbia.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Puedes agregar métodos de consulta personalizados si es necesario
}
