package com.nocountry.urbia.service.impl;

import com.nocountry.urbia.dto.request.LoginRequest;
import com.nocountry.urbia.dto.request.UsuarioRegistroRequest;
import com.nocountry.urbia.dto.response.JwtResponse;
import com.nocountry.urbia.dto.response.UsuarioResponse;
import com.nocountry.urbia.exception.ValidacionException;
import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.repository.UsuariosRepository;
import com.nocountry.urbia.util.JwtUtil;
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

    public Usuarios findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Usuarios usuario = usuariosRepository.findByEmail(loginRequest.getEmail());
        if (usuario == null || !passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new ValidacionException("Credenciales inválidas");
        }
        String token = jwtUtil.generateToken(usuario);
        return new JwtResponse(token, usuario.getId(), usuario.getEmail(), usuario.getNombre());
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
        return new UsuarioResponse(savedUsuario.getId(), savedUsuario.getNombre(), savedUsuario.getEmail(), usuario.getAvatarUrl());
    }

    // Métodos adicionales para gestión de usuarios
    public UsuarioResponse getUserById(Long id) {
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Usuario no encontrado"));
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getAvatarUrl());
    }

    public List<UsuarioResponse> getAllUsers() {
        List<UsuarioResponse> responseList = new ArrayList<>();
        usuariosRepository.findAll().forEach(usuario -> {
            responseList.add(new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getAvatarUrl()));
        });
        return responseList;
    }

    // Metodo para registrar un usuario usando datos de OAuth2 (sin contraseña)
    public Usuarios registerOAuthUser(String nombre, String email, String avatarUrl) {
        Usuarios usuario = new Usuarios();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(""); // No se necesita contraseña para OAuth2
        usuario.setAvatarUrl(avatarUrl);
        return usuariosRepository.save(usuario);
    }

    // Metodo para actualizar datos del usuario de google (como el avatar)
    public Usuarios updateUsuario(Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    // Metodo para eliminar un usuario
    public void deleteUser(Long id) {
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Usuario no encontrado"));
        usuariosRepository.delete(usuario);
    }

    // Metodo para actualizar un usuario, ya sea nombre o password
    public UsuarioResponse updateUser(Long id, UsuarioRegistroRequest updateRequest) {
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Usuario no encontrado"));

        usuario.setNombre(updateRequest.getNombre());
        // Si se envía un password nuevo (no vacío), se actualiza
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        Usuarios usuarioActualizado = usuariosRepository.save(usuario);
        return new UsuarioResponse(usuarioActualizado.getId(), usuarioActualizado.getNombre(), usuarioActualizado.getEmail(), usuarioActualizado.getAvatarUrl());
    }
}
