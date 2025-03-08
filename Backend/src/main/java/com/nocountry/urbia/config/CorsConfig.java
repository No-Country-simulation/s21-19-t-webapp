package com.nocountry.urbia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins(
            // Local development
            "http://localhost:3000",
            "http://127.0.0.1:3000",  // Added localhost IP with port 3000
            "http://127.0.0.1:5500",
            "http://localhost:5173",
            
            // Production environments
            "https://urbia.onrender.com",
            "https://prueba-post-reporte.vercel.app",
            "https://s21-19-t-webapp-ek59.onrender.com",
            "https://urbia-bug.onrender.com",
            "https://handsome-wisdom-production.up.railway.app"
        )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders(
                    "Origin",
                    "Content-Type",
                    "Accept",
                    "Authorization",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Headers",
                    "X-Requested-With"
                )
                .exposedHeaders(
                    "Authorization",
                    "Content-Disposition"
                )
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight requests for 1 hour
    }
}
