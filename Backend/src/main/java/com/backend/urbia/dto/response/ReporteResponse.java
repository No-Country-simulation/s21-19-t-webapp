package com.backend.urbia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ReporteResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private String resumenIA;
    private String ubicacion; // Se espera que este campo contenga un objeto GeoJSON en formato String
    private List<String> archivos;

}
