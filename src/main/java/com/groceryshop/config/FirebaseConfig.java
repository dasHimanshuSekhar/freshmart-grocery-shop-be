package com.groceryshop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource("firebase-service-account.json").getInputStream()
                        ))
                        .setDatabaseUrl("https://grocery-shop-336e0-default-rtdb.firebaseio.com")
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Firebase initialization failed: " + e.getMessage());
        }
    }
}
