package com.nocountry.urbia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reacciones")
public class Reaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Reporte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType tipo;

    @Column(name = "fecha_reaccion", nullable = false)
    private LocalDateTime fechaReaccion;

    public Reaccion() {
        this.fechaReaccion = LocalDateTime.now();
    }

    public Reaccion(Reporte reporte, Usuarios usuario, ReactionType tipo) {
        this.reporte = reporte;
        this.usuario = usuario;
        this.tipo = tipo;
        this.fechaReaccion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public Reporte getReporte() {
        return reporte;
    }
    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }
    public Usuarios getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
    public ReactionType getTipo() {
        return tipo;
    }
    public void setTipo(ReactionType tipo) {
        this.tipo = tipo;
    }
    public LocalDateTime getFechaReaccion() {
        return fechaReaccion;
    }
    public void setFechaReaccion(LocalDateTime fechaReaccion) {
        this.fechaReaccion = fechaReaccion;
    }
}
