package com.backend.urbia.controller;

import com.backend.urbia.dto.response.UsuarioResponse;
import com.backend.urbia.service.impl.UsuarioServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuarioServiceImpl usuarioService;

    @Autowired
    public UsuariosController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para obtener la lista de usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsers() {
        List<UsuarioResponse> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para obtener un usuario por su id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUserById(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.getUserById(id);
        return ResponseEntity.ok(usuario);
    }
}

