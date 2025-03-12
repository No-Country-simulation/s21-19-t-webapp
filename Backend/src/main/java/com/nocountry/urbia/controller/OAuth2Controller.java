package com.nocountry.urbia.controller;

import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.repository.UsuariosRepository;
import com.nocountry.urbia.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OAuth2Controller {

    @Value("${app.frontend.url.production}")
    private String productionUrl;

    @Value("${app.frontend.url.development}")
    private String developmentUrl;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/oauth2/callback/google")
    public RedirectView handleGoogleCallback(Authentication authentication, @RequestParam(required = false) String origin) {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            
            // Extraer información del usuario de Google
            Map<String, Object> attributes = oauth2User.getAttributes();
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String pictureUrl = (String) attributes.get("picture");
            
            // Buscar o crear usuario en la base de datos
            Usuarios usuario = usuariosRepository.findByEmail(email);
            if (usuario == null) {
                usuario = new Usuarios();
                usuario.setEmail(email);
                usuario.setNombre(name);
                usuario.setAvatarUrl(pictureUrl);
                usuario.setPassword(null);
                usuariosRepository.save(usuario);
            } else if (usuario.getAvatarUrl() == null) {
                usuario.setAvatarUrl(pictureUrl);
                usuariosRepository.save(usuario);
            }
            
            // Generar token JWT
            String token = jwtUtil.generateToken(usuario);
            
            // Crear objeto con datos del usuario
            Map<String, Object> userData = new HashMap<>();
            userData.put("token", token);
            userData.put("id", usuario.getId());
            userData.put("email", email);
            userData.put("name", name);
            userData.put("avatar", pictureUrl);
            
            // Convertir a JSON y codificar en Base64
            String userDataJson = objectMapper.writeValueAsString(userData);
            String encodedData = Base64.getUrlEncoder().encodeToString(userDataJson.getBytes());
            
            // Determinar la URL de redirección basada en el entorno
            String redirectUrl;
            if (origin != null && origin.contains("localhost")) {
                redirectUrl = developmentUrl;
            } else {
                redirectUrl = productionUrl;
            }
            
            // Redirigir al frontend con los datos codificados
            return new RedirectView(redirectUrl + "/auth/callback?data=" + encodedData);
            
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, redirigir a la página principal
            return new RedirectView(origin != null && origin.contains("localhost") 
                ? developmentUrl : productionUrl);
        }
    }
}