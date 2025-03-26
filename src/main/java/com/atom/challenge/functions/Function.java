package com.atom.challenge.functions;

import com.atom.challenge.infrastructure.JwtUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class Function {

    protected static final String DATABASE_URL = System.getenv("DATABASE_URL") != null
            ? System.getenv("DATABASE_URL")
            : "https://clusterdb-eaaa6.firebaseio.com"; // Valor por defecto

    protected static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT") != null
            ? System.getenv("GOOGLE_CLOUD_PROJECT")
            : "clusterdb-eaaa6";

    protected final Firestore firestore;

    protected final Gson gson = new Gson();
    protected final JwtUtil jwtUtil = new JwtUtil();

    public Function() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl(DATABASE_URL)
                .setProjectId(PROJECT_ID)
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        this.firestore = FirestoreClient.getFirestore();
    }

    protected <T> void response(HttpResponse response, T data, int status) throws IOException {
        response.setContentType("application/json");
        response.appendHeader("Access-Control-Allow-Origin", "*");
        response.appendHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.appendHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setStatusCode(status);
        response.getWriter().write(gson.toJson(data));
    }

    protected void handleOptions(HttpRequest request, HttpResponse response) throws IOException {
        response.appendHeader("Access-Control-Allow-Origin", "*");
        response.appendHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.appendHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.appendHeader("Access-Control-Max-Age", "3600");
        response.setStatusCode(204);
    }

    protected <T> T bodyFromRequest(HttpRequest request, Class<T> clazz) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        return gson.fromJson(body, clazz);
    }

    protected String getUserIdFromRequest(HttpRequest request) {
        String authHeader = request.getFirstHeader("Authorization").orElse("");
        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        return jwtUtil.getUserIdFromToken(token);
    }

    protected String extractTaskId(String path) {
        return Optional.of(path.split("/"))
                .map(parts -> parts[parts.length - 1])
                .orElseThrow(() -> new IllegalArgumentException("Invalid path"));
    }
}
