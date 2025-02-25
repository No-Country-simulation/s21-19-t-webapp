package com.backend.urbia.controller;

import com.backend.urbia.dto.request.ReporteRequest;
import com.backend.urbia.dto.response.ReporteResponse;
import com.backend.urbia.service.impl.ReporteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final ReporteServiceImpl reporteService;

    @Autowired
    public ReportesController(ReporteServiceImpl reporteService) {

        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<ReporteResponse> createReporte(
            @RequestPart("archivos") MultipartFile[] archivos,
            @RequestPart ReporteRequest reporteRequest) {
        ReporteResponse response = reporteService.createReporte(reporteRequest,archivos);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Puedes agregar aquí endpoints adicionales para obtener o actualizar reportes.
}
