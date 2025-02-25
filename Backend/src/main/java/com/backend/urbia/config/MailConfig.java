package com.backend.urbia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configura tu servidor de correo (esto es solo un ejemplo)
        mailSender.setHost("smtp.example.com"); // Cambia esto por tu servidor SMTP
        mailSender.setPort(8080); // Cambia esto por el puerto correcto

        // Configura las credenciales de autenticación si es necesario
        mailSender.setUsername("your-email@example.com"); // Tu dirección de correo
        mailSender.setPassword("your-email-password"); // Tu contraseña de correo

        // Configura las propiedades adicionales del correo
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}