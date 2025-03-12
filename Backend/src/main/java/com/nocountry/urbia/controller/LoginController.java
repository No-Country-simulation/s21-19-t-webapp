package com.nocountry.urbia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Redirige al flujo de autenticación de Google
        return "redirect:/oauth2/authorization/google";
    }


}
