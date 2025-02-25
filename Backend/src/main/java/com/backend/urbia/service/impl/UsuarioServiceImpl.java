package com.backend.urbia.service.impl;

import com.backend.urbia.dto.request.LoginRequest;
import com.backend.urbia.dto.request.UsuarioRegistroRequest;
import com.backend.urbia.dto.response.JwtResponse;
import com.backend.urbia.dto.response.UsuarioResponse;
import com.backend.urbia.exception.ValidacionException;
import com.backend.urbia.model.Usuarios;
import com.backend.urbia.repository.UsuariosRepository;
import com.backend.urbia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl {

    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioServiceImpl(UsuariosRepository usuariosRepository,
                              BCryptPasswordEncoder passwordEncoder,
                              JwtUtil jwtUtil) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Usuarios usuario = usuariosRepository.findByEmail(loginRequest.getEmail());
        if (usuario == null || !passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new ValidacionException("Credenciales inválidas");
        }
        String token = jwtUtil.generateToken(usuario);
        return new JwtResponse(token, usuario.getId(), usuario.getEmail());
    }

    public UsuarioResponse registerUser(UsuarioRegistroRequest request) {
        if (usuariosRepository.existsByEmail(request.getEmail())) {
            throw new ValidacionException("El email ya está en uso");
        }
        Usuarios usuario = new Usuarios();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        Usuarios savedUsuario = usuariosRepository.save(usuario);
        return new UsuarioResponse(savedUsuario.getId(), savedUsuario.getNombre(), savedUsuario.getEmail());
    }

    // Métodos adicionales para gestión de usuarios
    public UsuarioResponse getUserById(Long id) {
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Usuario no encontrado"));
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }

    public List<UsuarioResponse> getAllUsers() {
        List<UsuarioResponse> responseList = new ArrayList<>();
        usuariosRepository.findAll().forEach(usuario -> {
            responseList.add(new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail()));
        });
        return responseList;
    }
}
