package com.groceryshop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    private static final String SECRET_NAME = "projects/959644206209/secrets/grocey-backend-secrets/versions/latest";
    private static final String DATABASE_URL = "https://grocery-shop-336e0-default-rtdb.firebaseio.com";

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Fetch secret from Google Secret Manager
                SecretManagerServiceClient client = SecretManagerServiceClient.create();
                AccessSecretVersionResponse response = client.accessSecretVersion(SECRET_NAME);
                String firebaseKeyJson = response.getPayload().getData().toStringUtf8();

                System.out.println(firebaseKeyJson);

                // Convert JSON string into InputStream
                ByteArrayInputStream serviceAccount = new ByteArrayInputStream(firebaseKeyJson.getBytes(StandardCharsets.UTF_8));

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(DATABASE_URL)
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully using Secret Manager!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Firebase initialization failed: " + e.getMessage());
        }
    }
}
