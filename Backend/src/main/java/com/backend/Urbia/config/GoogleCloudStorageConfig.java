package com.backend.urbia.config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;

public class GoogleCloudStorageConfig {
    public static Storage getStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"))
        );
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
