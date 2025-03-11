package com.nocountry.urbia.controller;

import com.nocountry.urbia.dto.request.LoginRequest;
import com.nocountry.urbia.dto.request.UsuarioRegistroRequest;
import com.nocountry.urbia.dto.response.JwtResponse;
import com.nocountry.urbia.dto.response.UsuarioResponse;
import com.nocountry.urbia.security.TokenBlacklistService;
import com.nocountry.urbia.service.impl.UsuarioServiceImpl;
import com.nocountry.urbia.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class  AuthController {

    private final UsuarioServiceImpl usuarioService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;


    @Autowired
    public AuthController(UsuarioServiceImpl usuarioService, JwtUtil jwtUtil, TokenBlacklistService tokenBlacklistService) {

        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = usuarioService.authenticate(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registerUser(@RequestBody UsuarioRegistroRequest signUpRequest) {
        UsuarioResponse usuarioResponse = usuarioService.registerUser(signUpRequest);
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }
    
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Este es un endpoint público que no requiere autenticación");
        return ResponseEntity.ok(response);
    }

    // Endpoint de logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7);

            Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
            long expirationMillis = expirationDate.getTime() - System.currentTimeMillis();
            if (expirationMillis > 0) {
                tokenBlacklistService.blacklistToken(token, expirationMillis);
            }
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logout exitoso");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No se encontró token");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}

