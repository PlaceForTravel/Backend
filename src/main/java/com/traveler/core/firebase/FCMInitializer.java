package com.traveler.core.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() throws IOException {
        // initialize Admin SDK using OAuth 2.0 refresh token

        // Load the JSON file from the classpath
        ClassPathResource resource = new ClassPathResource("firebase/firebase_key.json");
        InputStream refreshToken = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setProjectId("traveling-e8283")
                .build();

        FirebaseApp.initializeApp(options);
    }
}
