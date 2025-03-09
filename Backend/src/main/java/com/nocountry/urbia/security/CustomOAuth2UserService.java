package com.nocountry.urbia.security;

import com.nocountry.urbia.model.Usuarios;
import com.nocountry.urbia.repository.UsuariosRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsuariosRepository usuariosRepository;

    public CustomOAuth2UserService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        Map<String, Object> attributes = oAuth2User.getAttributes();


        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("picture");  //  URL del avatar


        Usuarios usuario = usuariosRepository.findByEmail(email);
        if (usuario == null) {
            usuario = new Usuarios();
            usuario.setEmail(email);
            usuario.setNombre(name);

            usuario.setPassword("");
            // Si tienes un campo para avatar, puedes asignarlo:
            // usuario.setAvatarUrl(avatarUrl);
            usuario = usuariosRepository.save(usuario);
        } else {

            usuario.setNombre(name);
            // usuario.setAvatarUrl(avatarUrl);
            usuario = usuariosRepository.save(usuario);
        }

        // Opcional: agregar el ID del usuario local a los atributos para usarlo luego en la generación del token
        attributes.put("localUserId", usuario.getId());

        // Retornamos un OAuth2User. El tercer parámetro ("sub") indica qué atributo se usará como identificador único.
        return new DefaultOAuth2User(Collections.emptyList(), attributes, "sub");
    }
}
