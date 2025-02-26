package com.nocountry.urbia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {
    private Long id;
    private String urlAudio;
    private String urlImagen;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaHora;
    private Double latitud;
    private Double longitud;
    private Long categoriaId;
    private Long usuarioId;

}
