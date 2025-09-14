package com.Camello.Camello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CorsController {

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            return ResponseEntity.ok("Backend is running!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Object> status() {
        try {
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("status", "OK");
                put("timestamp", java.time.Instant.now().toString());
                put("version", "1.0.0");
            }});
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}