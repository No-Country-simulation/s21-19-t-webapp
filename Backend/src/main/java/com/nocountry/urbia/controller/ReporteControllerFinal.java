package com.nocountry.urbia.controller;

import com.nocountry.urbia.dto.request.ReporteDTO;
import com.nocountry.urbia.service.integration.S3Service;
import com.nocountry.urbia.service.impl.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/reporte")
public class ReporteControllerFinal {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private S3Service s3Service;  // Se agrega la inyección de S3Service

    @Autowired
    private WebSocketController webSocketController;

    // Endpoint para crear un reporte
    @PostMapping
    public ResponseEntity<ReporteDTO> crearReporte(@RequestBody ReporteDTO reporteDTO) {
        ReporteDTO nuevoReporte = reporteService.crearReporte(reporteDTO);
        // En caso de que no se elimine este endpoint agregar:
        // webSocketController.notificarNuevoReporte("Nuevo reporte creado");

        return new ResponseEntity<>(nuevoReporte, HttpStatus.CREATED);
    }

    // Endpoint para listar todos los reportes
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listarReportes() {
        List<ReporteDTO> reportes = reporteService.listarReportes();
        return ResponseEntity.ok(reportes);
    }

    // Endpoint para actualizar un reporte con archivos (audio e imagen)
    @PutMapping(value = "{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ReporteDTO> actualizarReporte(
            @PathVariable Long id,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestPart("reporte") ReporteDTO reporteDTO) {
        return reporteService.actualizarReporteConImagenIA(id, audio, imagen, reporteDTO);
    }

    // Endpoint para eliminar todos los reportes
    @DeleteMapping("/eliminar-reportes")
    public ResponseEntity<String> eliminarTodosLosReportes() {
        try {
            reporteService.eliminarTodosLosReportes();
            return ResponseEntity.ok("Todos los reportes han sido eliminados.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hubo un error al eliminar los reportes: " + e.getMessage());
        }
    }

    // Endpoint para eliminar un reporte
    @DeleteMapping("id/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }



    // Endpoint para obtener reportes por usuarioId
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReporteDTO>> getReportesByUsuario(@PathVariable Long usuarioId) {
        List<ReporteDTO> reportes = reporteService.getReportesByUsuario(usuarioId);
        return ResponseEntity.ok(reportes);
    }

    // Endpoint combinado para cargar imagen y crear reporte
    @PostMapping("/combinado")
    public ResponseEntity<?> crearReporteConImagen(
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestPart("reporte") ReporteDTO reporteDTO) {

        if (reporteDTO == null) {
            return ResponseEntity.badRequest().body("El objeto reporte es obligatorio.");
        }

        // Validar archivo de audio, si se envía
        if (audio != null && !audio.isEmpty()) {
            String audioContentType = audio.getContentType();
            if (!( "audio/wav".equals(audioContentType) ||
                    "audio/mpeg".equals(audioContentType) ||  // Agregado para MP3
                    "audio/aiff".equals(audioContentType) ||
                    "audio/aac".equals(audioContentType) ||
                    "audio/ogg".equals(audioContentType) ||
                    "audio/flac".equals(audioContentType) )) {
                String message = "Formato de audio no soportado. Formatos compatibles: WAV (audio/wav), MP3 (audio/mp3), AIFF (audio/aiff), AAC (audio/aac), OGG (audio/ogg) y FLAC (audio/flac).";
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(message);
            }
        }

        // Validar archivo de imagen, si se envía
        if (imagen != null && !imagen.isEmpty()) {
            String imageContentType = imagen.getContentType();
            if (!( "image/png".equals(imageContentType) ||
                    "image/jpeg".equals(imageContentType) ||
                    "image/webp".equals(imageContentType) ||
                    "image/heic".equals(imageContentType) ||
                    "image/heif".equals(imageContentType) )) {
                String message = "Formato de imagen no soportado. Formatos compatibles: PNG (image/png), JPEG (image/jpeg), WEBP (image/webp), HEIC (image/heic) y HEIF (image/heif).";
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(message);
            }
        }

        // Si la validación es correcta, continúa con la lógica del servicio
        return reporteService.getReportesIA(audio, imagen, reporteDTO);
    }


}
