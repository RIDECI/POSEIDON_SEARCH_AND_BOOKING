package edu.dosw.rideci.infrastructure.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides basic health and info endpoints for the service
 */
@RestController
public class HealthController {

    /**
     * Root endpoint - Service information
     * @return Service details
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "POSEIDON Search and Booking");
        response.put("status", "UP");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("endpoints", Map.of(
                "health", "/health",
                "api", "/api/v1/bookings",
                "swagger", "/swagger-ui.html",
                "api-docs", "/api-docs"
        ));
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     * @return Service health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * API Info endpoint
     * @return API information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "POSEIDON Search and Booking API");
        response.put("description", "Microservice for travel search and booking operations");
        response.put("version", "1.0.0");
        response.put("team", "POSEIDON");
        response.put("baseUrl", "/api/v1");
        return ResponseEntity.ok(response);
    }
}