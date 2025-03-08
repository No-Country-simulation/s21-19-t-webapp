package com.nocountry.urbia.dto.request;

import com.nocountry.urbia.model.ReactionType;

public class ReactionRequest {
    private Long idReporte;
    private Long idUsuario;
    private ReactionType tipo;

    // Getters y Setters
    public Long getIdReporte() {
        return idReporte;
    }
    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }
    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    public ReactionType getTipo() {
        return tipo;
    }
    public void setTipo(ReactionType tipo) {
        this.tipo = tipo;
    }
}
