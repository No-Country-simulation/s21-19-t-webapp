package com.nocountry.urbia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL de la imagen
    @Column(name = "url_imagen")
    private String urlImagen;

    // URL del audio
    @Column(name = "url_audio")
    private String urlAudio;

    // URL del video
    @Column(name = "url_video")
    private String urlVideo;

    // Titulo del incidente
    @Column(length = 500)
    private String titulo;

    // Descripción original del incidente
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Nueva columna: Descripción después de aplicar IA
    @Column(name = "descripcion_despues_de_ia", columnDefinition = "TEXT")
    private String descripcionDespuesDeIa;

    // Fecha y hora del reporte
    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    // Coordenadas: latitud y longitud
    private Double latitud;
    private Double longitud;

    // Relación con Categoría
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    // Relación con Reacciones: eliminación en cascada
    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaccion> reacciones = new ArrayList<>();

    // Getters y Setters

    public Reporte() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlAudio() {
        return urlAudio;
    }

    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getDescripcionDespuesDeIa() {
        return descripcionDespuesDeIa;
    }

    public void setDescripcionDespuesDeIA(String descripcionMejorada) {
        this.descripcionDespuesDeIa = descripcionMejorada;
    }

    public List<Reaccion> getReacciones() {
        return reacciones;
    }

    public void setReacciones(List<Reaccion> reacciones) {
        this.reacciones = reacciones;
    }
}
