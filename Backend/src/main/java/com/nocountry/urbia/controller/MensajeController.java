package com.nocountry.urbia.controller;

import com.nocountry.urbia.model.Mensaje;
import com.nocountry.urbia.service.impl.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    // Crear un nuevo mensaje
    @PostMapping
    public ResponseEntity<Mensaje> crearMensaje(@RequestBody Mensaje mensaje) {
        Mensaje nuevoMensaje = mensajeService.crearMensaje(mensaje);
        return new ResponseEntity<>(nuevoMensaje, HttpStatus.CREATED);
    }

    // Obtener todos los mensajes de un reporte específico
    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<List<Mensaje>> obtenerMensajesPorReporte(@PathVariable Long reporteId) {
        List<Mensaje> mensajes = mensajeService.obtenerMensajesPorReporte(reporteId);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    // Obtener un mensaje por su id
    @GetMapping("/{id}")
    public ResponseEntity<Mensaje> obtenerMensajePorId(@PathVariable Long id) {
        Optional<Mensaje> mensaje = mensajeService.obtenerMensajePorId(id);
        return mensaje.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Actualizar un mensaje existente
    @PutMapping("/{id}")
    public ResponseEntity<Mensaje> actualizarMensaje(@PathVariable Long id, @RequestBody Mensaje mensaje) {
        Optional<Mensaje> mensajeExistente = mensajeService.obtenerMensajePorId(id);
        if(mensajeExistente.isPresent()){
            Mensaje mensajeActualizar = mensajeExistente.get();
            mensajeActualizar.setContenido(mensaje.getContenido());
            // Se pueden actualizar otros campos según sea necesario
            Mensaje actualizado = mensajeService.actualizarMensaje(mensajeActualizar);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un mensaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        mensajeService.eliminarMensaje(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
