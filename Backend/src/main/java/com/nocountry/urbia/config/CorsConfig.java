package com.nocountry.urbia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        "https://urbia.onrender.com/",
                        "http://127.0.0.1:5500",
                        "https://prueba-post-reporte.vercel.app/",
                        "https://s21-19-t-webapp-ek59.onrender.com/",
                        "https://urbia-bug.onrender.com/") // Cambia este valor según el dominio de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
