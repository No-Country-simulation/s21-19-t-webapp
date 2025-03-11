package com.nocountry.urbia.service.impl;

import com.nocountry.urbia.model.Mensaje;
import com.nocountry.urbia.repository.MensajeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public Mensaje crearMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesPorReporte(Long reporteId) {
        return mensajeRepository.findByReporteId(reporteId);
    }

    public Optional<Mensaje> obtenerMensajePorId(Long id) {
        return mensajeRepository.findById(id);
    }

    public Mensaje actualizarMensaje(Mensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public void eliminarMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }
}
