package com.nocountry.urbia.security;

import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.service.impl.UsuarioServiceImpl;
import com.nocountry.urbia.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UsuarioServiceImpl usuarioService;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, UsuarioServiceImpl usuarioService) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Recuperar el usuario autenticado a través de OAuth2
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Extraer los atributos relevantes de la respuesta de Google:
        String email = (String) attributes.get("email");
        String nombre = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("picture");

        // Buscar usuario en la BD
        Usuarios usuario = usuarioService.findByEmail(email);
        if (usuario == null) {

            usuario = usuarioService.registerOAuthUser(nombre, email, avatarUrl);
        } else {

            usuario.setNombre(nombre);
            usuario.setAvatarUrl(avatarUrl);
            usuario = usuarioService.updateUsuario(usuario);
        }

        // Generar token JWT usando el usuario obtenido/registrado
        String token = jwtUtil.generateToken(usuario);


        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("type", "Bearer");
        responseBody.put("email", email);
        responseBody.put("nombre", nombre);
        responseBody.put("avatar", avatarUrl);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
