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
                    // Development environments
                    "http://localhost:3000",
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:5500",
                    "http://localhost:5173",
                    "http://localhost:8080",
                    
                    // Production environments
                    "https://urbia.onrender.com",
                    "https://prueba-post-reporte.vercel.app",
                    "https://s21-19-t-webapp-ek59.onrender.com",
                    "https://handsome-wisdom-production.up.railway.app",
                    "https://api-urbia.up.railway.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")  // Allow all headers for now to fix CORS issues
                .exposedHeaders(
                    "Access-Control-Allow-Origin", 
                    "Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight requests for 1 hour
    }
}
