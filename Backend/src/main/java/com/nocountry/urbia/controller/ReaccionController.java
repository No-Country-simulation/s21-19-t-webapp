package com.nocountry.urbia.controller;

import com.nocountry.urbia.dto.request.ReactionRequest;
import com.nocountry.urbia.dto.response.ReactionSummaryResponse;
import com.nocountry.urbia.service.impl.ReaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reacciones")
public class ReaccionController {

    @Autowired
    private ReaccionService reaccionService;

    /**
     * POST /api/reacciones/reaccionar
     * Recibe un JSON:
     * {
     *   "idReporte": 246,
     *   "idUsuario": 2,
     *   "tipo": "RECOMENDAR"
     * }
     * Crea la reaccion (si no existe) y retorna el resumen actualizado de reacciones.
     */
    @PostMapping("/reaccionar")
    public ResponseEntity<?> agregarReaccion(@RequestBody ReactionRequest request) {
        try {
            ReactionSummaryResponse response = reaccionService.agregarReaccion(
                    request.getIdReporte(),
                    request.getIdUsuario(),
                    request.getTipo());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PUT /api/reacciones/reaccionar
     * Permite modificar la reacción del usuario (única por reporte).
     * Recibe el mismo JSON que el POST y retorna el resumen actualizado.
     */
    @PutMapping("/reaccionar")
    public ResponseEntity<?> modificarReaccion(@RequestBody ReactionRequest request) {
        try {
            ReactionSummaryResponse response = reaccionService.modificarReaccion(
                    request.getIdReporte(),
                    request.getIdUsuario(),
                    request.getTipo());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET /api/reacciones/resumen/{idReporte}
     * Retorna un JSON con el número de reacciones por tipo y los nombres de los usuarios que reaccionaron.
     */
    @GetMapping("/resumen/{idReporte}")
    public ResponseEntity<?> obtenerResumen(@PathVariable Long idReporte) {
        try {
            ReactionSummaryResponse response = reaccionService.obtenerResumenReacciones(idReporte);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
