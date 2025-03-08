package com.nocountry.urbia.service.impl;

import com.nocountry.urbia.dto.response.ReactionSummaryResponse;
import com.nocountry.urbia.model.Reaccion;
import com.nocountry.urbia.model.ReactionType;
import com.nocountry.urbia.model.Reporte;
import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.repository.ReaccionRepository;
import com.nocountry.urbia.repository.ReporteRepository;
import com.nocountry.urbia.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReaccionService {

    @Autowired
    private ReaccionRepository reaccionRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuariosRepository usuarioRepository;

    // POST: Agregar una reacción nueva (si no existe) y retornar el resumen actualizado.
    public ReactionSummaryResponse agregarReaccion(Long idReporte, Long idUsuario, ReactionType tipo) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (reaccionRepository.findByReporteAndUsuario(reporte, usuario).isPresent()) {
            throw new RuntimeException("El usuario ya ha reaccionado a este reporte");
        }
        Reaccion reaccion = new Reaccion(reporte, usuario, tipo);
        reaccionRepository.save(reaccion);
        return obtenerResumenReacciones(idReporte);
    }

    // PUT: Modificar la reacción existente del usuario y retornar el resumen actualizado.
    public ReactionSummaryResponse modificarReaccion(Long idReporte, Long idUsuario, ReactionType tipo) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        Usuarios usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reaccion reaccion = reaccionRepository.findByReporteAndUsuario(reporte, usuario)
                .orElseThrow(() -> new RuntimeException("La reacción no existe para este usuario"));
        reaccion.setTipo(tipo);
        reaccionRepository.save(reaccion);
        return obtenerResumenReacciones(idReporte);
    }

    // GET: Obtener resumen de reacciones para un reporte.
    public ReactionSummaryResponse obtenerResumenReacciones(Long idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        List<Reaccion> reacciones = reaccionRepository.findByReporte(reporte);

        ReactionSummaryResponse summary = new ReactionSummaryResponse();

        reacciones.forEach(r -> {
            String nombreUsuario = r.getUsuario().getNombre(); // Se asume que Usuarios tiene getNombre()
            switch (r.getTipo()) {
                case RECOMENDAR:
                    summary.getRecomendar().setCount(summary.getRecomendar().getCount() + 1);
                    summary.getRecomendar().getUsuarios().add(nombreUsuario);
                    break;
                case CELEBRAR:
                    summary.getCelebrar().setCount(summary.getCelebrar().getCount() + 1);
                    summary.getCelebrar().getUsuarios().add(nombreUsuario);
                    break;
                case APOYAR:
                    summary.getApoyar().setCount(summary.getApoyar().getCount() + 1);
                    summary.getApoyar().getUsuarios().add(nombreUsuario);
                    break;
                case DESAPROBAR:
                    summary.getDesaprobar().setCount(summary.getDesaprobar().getCount() + 1);
                    summary.getDesaprobar().getUsuarios().add(nombreUsuario);
                    break;
            }
        });
        return summary;
    }
}
