package com.backend.urbia.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudStorageBeanConfig {

    @Value("${GOOGLE_APPLICATION_CREDENTIALS:}")
    private String credentialsPath;

    @Value("${GOOGLE_CREDENTIALS_JSON:}")
    private String credentialsJson;

    @Bean
    public Storage googleCloudStorage() throws IOException {
        GoogleCredentials credentials;
        if (!credentialsJson.isEmpty()) {
            // Si se proporciona el JSON directamente en la variable de entorno
            try (InputStream inputStream = new java.io.ByteArrayInputStream(credentialsJson.getBytes())) {
                credentials = GoogleCredentials.fromStream(inputStream);
            }
        } else if (!credentialsPath.isEmpty()) {
            // Si se proporciona la ruta al archivo JSON
            try (InputStream inputStream = new FileInputStream(credentialsPath)) {
                credentials = GoogleCredentials.fromStream(inputStream);
            }
        } else {
            // Si no se proporciona ninguna credencial, usar las credenciales predeterminadas
            credentials = GoogleCredentials.getApplicationDefault();
        }

        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}

