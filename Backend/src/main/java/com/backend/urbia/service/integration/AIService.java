package com.backend.urbia.service.integration;

import com.backend.urbia.dto.request.ReporteRequest;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String generateSummary(ReporteRequest reporteRequest) {
        // Aquí implementarías la llamada a la API de IA para generar un resumen basado en la descripción o archivos.
        // Por ejemplo, utilizando RestTemplate o WebClient para consumir la API externa.
        String descripcion = reporteRequest.getDescripcion();
        if (descripcion != null && descripcion.length() > 100) {
            return descripcion.substring(0, 100) + "...";
        }
        return descripcion != null ? descripcion : "Resumen no disponible";
    }
}
