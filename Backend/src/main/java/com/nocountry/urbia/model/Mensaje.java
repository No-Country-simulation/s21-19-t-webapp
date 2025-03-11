package com.nocountry.urbia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contenido del mensaje
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    // Fecha y hora en que se creó el mensaje
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // Relación con el usuario que comenta
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    // Relación con el reporte que se comenta
    @ManyToOne
    @JoinColumn(name = "reporte_id", nullable = false)
    private Reporte reporte;

    // Constructores
    public Mensaje() {
        this.fechaHora = LocalDateTime.now();
    }

    public Mensaje(String contenido, Usuarios usuario, Reporte reporte) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.reporte = reporte;
        this.fechaHora = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }
}
