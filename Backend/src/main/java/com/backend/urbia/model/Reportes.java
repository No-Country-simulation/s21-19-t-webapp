package com.backend.urbia.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reportes")
public class Reportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000, nullable = false)
    private String descripcion;

    @Column(name = "resumen_ia")
    private String resumenIA;

    // Almacenamos la ubicación como un Point, con definición de columna para PostGIS
    @Column(columnDefinition = "geometry")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point ubicacion;


    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "reportes_archivos", joinColumns = @JoinColumn(name = "reporte_id"))
    @Column(name = "archivo_url")
    private List<String> archivos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    public Reportes() {}

    public Reportes(String titulo, String descripcion, String resumenIA, Point ubicacion, LocalDateTime fechaCreacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.resumenIA = resumenIA;
        this.ubicacion = ubicacion;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResumenIA() {
        return resumenIA;
    }

    public void setResumenIA(String resumenIA) {
        this.resumenIA = resumenIA;
    }

    public Point getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Point ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<String> archivos) {
        this.archivos = archivos;
    }

    public Usuarios getUsuario() {return usuario;
    }

    public void setUsuario(Usuarios usuario) {this.usuario = usuario;
    }
}
