package com.backend.urbia.service.impl;

import com.backend.urbia.dto.request.ReporteRequest;
import com.backend.urbia.dto.response.ReporteResponse;
import com.backend.urbia.exception.ValidacionException;
import com.backend.urbia.model.Reportes;
import com.backend.urbia.repository.ReportesRepository;
import com.backend.urbia.service.integration.AIService;
import com.backend.urbia.service.integration.GoogleCloudStorageService;
import com.backend.urbia.util.GeoUtils;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ReporteServiceImpl.class);

    private final ReportesRepository reportesRepository;
    private final AIService aiService;
    private final GoogleCloudStorageService googleCloudStorageService;

    public ReporteServiceImpl(ReportesRepository reportesRepository, AIService aiService, GoogleCloudStorageService googleCloudStorageService) {
        this.reportesRepository = reportesRepository;
        this.aiService = aiService;
        this.googleCloudStorageService = googleCloudStorageService;
    }

    @Transactional
    public ReporteResponse createReporte(ReporteRequest reporteRequest, MultipartFile[] archivos) {
        logger.info("Iniciando creación de reporte...");

        // Validaciones básicas
        if (reporteRequest.getTitulo() == null || reporteRequest.getTitulo().isEmpty()) {
            throw new ValidacionException("El título es obligatorio");
        }

        // Convertir coordenadas (lng, lat) a Point
        Point ubicacion = GeoUtils.createPoint(reporteRequest.getLongitud(), reporteRequest.getLatitud());

        // Generar resumen mediante el servicio de IA
        String resumenIA = aiService.generateSummary(reporteRequest);

        // Subir archivos a Google Cloud Storage
        List<String> fileUrls = new ArrayList<>();
        if (archivos != null) {
            for (MultipartFile archivo : archivos) {
                try {
                    String fileUrl = googleCloudStorageService.uploadFileToGCS(archivo);
                    fileUrls.add(fileUrl);
                    logger.info("Archivo subido con éxito: {}", fileUrl);
                } catch (IOException e) {
                    logger.error("Error subiendo archivo: {}", archivo.getOriginalFilename(), e);
                }
            }
        }

        // Crear la entidad Reportes
        Reportes reporte = new Reportes();
        reporte.setTitulo(reporteRequest.getTitulo());
        reporte.setDescripcion(reporteRequest.getDescripcion());
        reporte.setResumenIA(resumenIA);
        reporte.setUbicacion(ubicacion);
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setArchivos(fileUrls);

        // Guardar en la base de datos
        Reportes savedReporte = reportesRepository.save(reporte);
        logger.info("Reporte guardado en la base de datos con ID: {}", savedReporte.getId());

        // Mapear a DTO de respuesta
        ReporteResponse response = new ReporteResponse();
        response.setId(savedReporte.getId());
        response.setTitulo(savedReporte.getTitulo());
        response.setDescripcion(savedReporte.getDescripcion());
        response.setResumenIA(savedReporte.getResumenIA());
        response.setUbicacion(GeoUtils.convertPointToGeoJSON(savedReporte.getUbicacion()));
        response.setArchivos(savedReporte.getArchivos());

        return response;
    }
}


