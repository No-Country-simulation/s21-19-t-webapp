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
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

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

    @GetMapping("/oauth2/callback/google")
    public RedirectView handleGoogleCallback(Authentication authentication) {
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
            // Establecer una contraseña aleatoria o null si no se requiere
            usuario.setPassword(null);
            usuariosRepository.save(usuario);
        }
        
        // Generar token JWT
        String token = jwtUtil.generateToken(usuario);
        
        // Determinar la URL de redirección basada en el entorno
        String redirectUrl;
        String requestOrigin = System.getProperty("request.origin");
        
        if (requestOrigin != null && requestOrigin.contains("localhost")) {
            redirectUrl = developmentUrl;
        } else {
            redirectUrl = productionUrl;
        }
        
        // Agregar el token como parámetro de consulta
        return new RedirectView(redirectUrl + "/auth/callback?token=" + token);
    }
}