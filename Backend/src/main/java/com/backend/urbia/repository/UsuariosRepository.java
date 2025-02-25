package com.backend.urbia.repository;

import com.backend.urbia.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    boolean existsByEmail(String email);
    Usuarios findByEmail(String email);
}
