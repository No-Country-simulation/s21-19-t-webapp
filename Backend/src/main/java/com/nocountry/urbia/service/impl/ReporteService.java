package com.nocountry.urbia.service.impl;

import com.nocountry.urbia.dto.request.ReporteDTO;
import com.nocountry.urbia.model.Categoria;
import com.nocountry.urbia.model.Reporte;
import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.repository.CategoriaRepository;
import com.nocountry.urbia.repository.ReporteRepository;
import com.nocountry.urbia.repository.UsuariosRepository;
import com.nocountry.urbia.service.integration.GeminiService;
import com.nocountry.urbia.service.integration.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private GeminiService geminiService; // Inyección del servicio Gemini

    @Autowired
    private S3Service s3Service;  // Para la carga de imágenes

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

    // Crear reporte y analizar con IA
    public ResponseEntity<ReporteDTO> getReportesIA(MultipartFile audio, MultipartFile imagen, ReporteDTO reporteDTO) {
        // Validar que la descripción no sea nula o vacía
        if (reporteDTO.getDescripcion() == null || reporteDTO.getDescripcion().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String detallesArchivo = "";

        // Procesar el archivo de audio, si se envió
        if (audio != null && !audio.isEmpty()) {
            String audioContentType = audio.getContentType();
            if (audioContentType == null || !audioContentType.startsWith("audio/")) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            // Subir audio a S3 y obtener URL
            String audioUrl = s3Service.uploadFile(audio);
            reporteDTO.setUrlAudio(audioUrl);
            // Analizar el audio con Gemini
            String detalleAudio = geminiService.analizarAudioPublica(audioUrl);
            if (!detalleAudio.isEmpty()) {
                detallesArchivo += "Audio: " + detalleAudio + "\n";
            }
        }

        // Procesar el archivo de imagen, si se envió
        if (imagen != null && !imagen.isEmpty()) {
            String imagenContentType = imagen.getContentType();
            if (imagenContentType == null || !imagenContentType.startsWith("image/")) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            // Subir imagen a S3 y obtener URL
            String imageUrl = s3Service.uploadFile(imagen);
            reporteDTO.setUrlImagen(imageUrl);
            // Analizar la imagen con Gemini
            String detalleImagen = geminiService.analizarImagenPublica(imageUrl);
            if (!detalleImagen.isEmpty()) {
                detallesArchivo += "Imagen: " + detalleImagen + "\n";
            }
        }

        // Combinar título y descripción, incluyendo los detalles de IA
        String descripcionConTitulo = "Título: " + reporteDTO.getTitulo() + "\nDescripción: " + reporteDTO.getDescripcion();
        reporteDTO.setDescripcion(descripcionConTitulo + "\nDetalles IA: " + detallesArchivo);

        // Crear el reporte
        ReporteDTO nuevoReporte = crearReporte(reporteDTO);
        return new ResponseEntity<>(nuevoReporte, HttpStatus.CREATED);
    }





    // Crear reporte
    public ReporteDTO crearReporte(ReporteDTO reporteDTO) {
        Reporte reporte = new Reporte();
        reporte.setUrlAudio(reporteDTO.getUrlAudio());
        reporte.setUrlImagen(reporteDTO.getUrlImagen());
        reporte.setUrlVideo(reporteDTO.getUrlVideo());
        reporte.setTitulo(reporteDTO.getTitulo());
        reporte.setDescripcion(reporteDTO.getDescripcion());
        // Se asigna la fecha y hora actuales
        reporte.setFechaHora(LocalDateTime.now());
        reporte.setLatitud(reporteDTO.getLatitud());
        reporte.setLongitud(reporteDTO.getLongitud());

        // Buscar y asignar la categoría
        Categoria categoria = categoriaRepository.findById(reporteDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        reporte.setCategoria(categoria);

        // Buscar y asignar el usuario
        Usuarios usuario = usuarioRepository.findById(reporteDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        reporte.setUsuario(usuario);

        // Guardar el reporte inicial
        Reporte reporteGuardado = reporteRepository.save(reporte);

        // Llamada a Gemini IA para mejorar la descripción
        String descripcionMejorada = geminiService.mejorarDescripcion(
                reporteGuardado.getDescripcion(),
                reporteGuardado.getTitulo(),
                reporteGuardado.getUrlImagen(),
                reporteGuardado.getUrlAudio());
        reporteGuardado.setDescripcionDespuesDeIA(descripcionMejorada);

        // Actualizar el reporte con la descripción mejorada
        reporteGuardado = reporteRepository.save(reporteGuardado);

        return mapearDTO(reporteGuardado);
    }

    // Listar todos los reportes
    public List<ReporteDTO> listarReportes() {
        List<Reporte> reportes = reporteRepository.findAll();
        return reportes.stream().map(this::mapearDTO).collect(Collectors.toList());
    }

    // Actualización combinada de reporte (PUT con archivos)
    public ResponseEntity<ReporteDTO> actualizarReporteConImagenIA(Long id, MultipartFile audio, MultipartFile imagen, ReporteDTO reporteDTO) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        String detallesArchivo = "";

        // Procesar el archivo de audio, si se envía
        if (audio != null && !audio.isEmpty()) {
            String audioContentType = audio.getContentType();
            if (audioContentType == null || !audioContentType.startsWith("audio/")) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            String audioUrl = s3Service.uploadFile(audio);
            reporteDTO.setUrlAudio(audioUrl);
            String detalleAudio = geminiService.analizarAudioPublica(audioUrl);
            if (!detalleAudio.isEmpty()) {
                detallesArchivo += "Audio: " + detalleAudio + "\n";
            }
            reporte.setUrlAudio(audioUrl);
        }

        // Procesar el archivo de imagen, si se envía
        if (imagen != null && !imagen.isEmpty()) {
            String imagenContentType = imagen.getContentType();
            if (imagenContentType == null || !imagenContentType.startsWith("image/")) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            String imageUrl = s3Service.uploadFile(imagen);
            reporteDTO.setUrlImagen(imageUrl);
            String detalleImagen = geminiService.analizarImagenPublica(imageUrl);
            if (!detalleImagen.isEmpty()) {
                detallesArchivo += "Imagen: " + detalleImagen + "\n";
            }
            reporte.setUrlImagen(imageUrl);
        }

        // Actualizar datos del reporte
        reporte.setTitulo(reporteDTO.getTitulo());
        String descripcionConTitulo = "Título: " + reporteDTO.getTitulo() + "\nDescripción: " + reporteDTO.getDescripcion();
        reporte.setDescripcion(descripcionConTitulo + "\nDetalles IA: " + detallesArchivo);
        reporte.setLatitud(reporteDTO.getLatitud());
        reporte.setLongitud(reporteDTO.getLongitud());

        if (reporteDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(reporteDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            reporte.setCategoria(categoria);
        }

        if (reporteDTO.getUsuarioId() != null) {
            Usuarios usuario = usuarioRepository.findById(reporteDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            reporte.setUsuario(usuario);
        }

        Reporte reporteActualizado = reporteRepository.save(reporte);

        // Llamada a Gemini IA para mejorar la descripción actualizada
        String descripcionMejorada = geminiService.mejorarDescripcion(
                reporteActualizado.getDescripcion(),
                reporteActualizado.getTitulo(),
                reporteActualizado.getUrlImagen(),
                reporteActualizado.getUrlAudio());
        reporteActualizado.setDescripcionDespuesDeIA(descripcionMejorada);

        reporteActualizado = reporteRepository.save(reporteActualizado);
        return new ResponseEntity<>(mapearDTO(reporteActualizado), HttpStatus.OK);
    }

    // Actualización sin archivos (mantiene la funcionalidad original)
    public ReporteDTO actualizarReporte(Long id, ReporteDTO reporteDTO) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        reporte.setUrlAudio(reporteDTO.getUrlAudio());
        reporte.setUrlImagen(reporteDTO.getUrlImagen());
        reporte.setDescripcion(reporteDTO.getDescripcion());
        // Se puede actualizar la fecha, o mantener la original según la lógica de negocio
        reporte.setLatitud(reporteDTO.getLatitud());
        reporte.setLongitud(reporteDTO.getLongitud());

        if (reporteDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(reporteDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            reporte.setCategoria(categoria);
        }

        if (reporteDTO.getUsuarioId() != null) {
            Usuarios usuario = usuarioRepository.findById(reporteDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            reporte.setUsuario(usuario);
        }

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return mapearDTO(reporteActualizado);
    }

    // Eliminar reporte
    public void eliminarReporte(Long id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporteRepository.delete(reporte);
    }

    // Método para obtener reportes por usuarioId
    public List<ReporteDTO> getReportesByUsuario(Long usuarioId) {
        List<Reporte> reportes = reporteRepository.findByUsuarioId(usuarioId);
        return reportes.stream().map(this::mapearDTO).collect(Collectors.toList());
    }

    // Método para mapear la entidad Reporte a ReporteDTO
    private ReporteDTO mapearDTO(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO();
        dto.setId(reporte.getId());
        dto.setUrlAudio(reporte.getUrlAudio());
        dto.setUrlImagen(reporte.getUrlImagen());
        dto.setUrlVideo(reporte.getUrlVideo());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setDescripcionDespuesDeIa(reporte.getDescripcionDespuesDeIa());
        dto.setFechaHora(reporte.getFechaHora());
        dto.setLatitud(reporte.getLatitud());
        dto.setLongitud(reporte.getLongitud());
        dto.setCategoriaId(reporte.getCategoria().getId());
        dto.setUsuarioId(reporte.getUsuario().getId());
        dto.setNombreUsuario(reporte.getUsuario().getNombre());
        return dto;
    }

    // Eliminar todos los reportes (y sus reacciones en cascada)
    public void eliminarTodosLosReportes() {
        long totalReportes = reporteRepository.count();
        if (totalReportes > 0) {
            reporteRepository.deleteAll();
            logger.info("Se eliminaron {} reportes.", totalReportes);
        } else {
            logger.warn("No hay reportes para eliminar.");
        }
    }


}
