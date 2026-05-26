package com.java.gatewayy.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;




@RestController
public class ProxyController {

    @Value("${auth.service.url}")
    private String authUrl;

    @Value("${user.service.url}")
    private String userUrl;

    @Value("${report.service.url}")
    private String reportUrl;

    @Value("${notification.service.url}")
    private String notificationUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProxyController() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);

        RestTemplate rt = new RestTemplate(factory);
        rt.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatusCode code) {
                return false;
            }
        });
        this.restTemplate = rt;
    }

    // ── AUTH público ──────────────────────────────────────

@PostMapping("/api/auth/register")
public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
    try {
        String json = objectMapper.writeValueAsString(body);
        System.out.println(">>> sending json: " + json);
        ResponseEntity<Object> response = restTemplate.postForEntity(
            authUrl + "/api/auth/register",
            new HttpEntity<>(json, jsonHeaders()),
            Object.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    } catch (Exception e) {
        System.out.println(">>> ERROR: " + e.getMessage());
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}

@PostMapping("/api/auth/login")
public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
    try {
        String json = objectMapper.writeValueAsString(body);
        ResponseEntity<Object> response = restTemplate.postForEntity(
            authUrl + "/api/auth/login",
            new HttpEntity<>(json, jsonHeaders()),
            Object.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}


//USERS
    @GetMapping("/api/users/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/profile",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @PutMapping("/api/users/update")
    public ResponseEntity<?> updateUser(HttpServletRequest req,
                                        @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/update",
            HttpMethod.PUT,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
    @GetMapping("/api/users/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/users",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @PutMapping("/api/users/users/{id}/admin")
        public ResponseEntity<?> updateUserAdmin(HttpServletRequest req,
                                                @PathVariable Long id,
                                                @RequestBody Map<String, Object> body) {
            ResponseEntity<String> response = restTemplate.exchange(
                userUrl + "/api/users/users/" + id + "/admin",
                HttpMethod.PUT,
                new HttpEntity<>(body, userHeaders(req)),
                String.class
            );
            return ResponseEntity.status(response.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());
        }


        //BRIGADGES

        @GetMapping("/api/users/brigadas")
        public ResponseEntity<?> getAllBrigades(HttpServletRequest req) {
            ResponseEntity<String> response = restTemplate.exchange(
                userUrl + "/api/users/brigadas",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders(req)),
                String.class
            );
            return ResponseEntity.status(response.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());
        }

        @PostMapping("/api/users/brigadas")
    public ResponseEntity<?> createBrigade(HttpServletRequest req,
                                           @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/brigadas",
            HttpMethod.POST,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity.status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
 
    @PutMapping("/api/users/brigadas/{id}")
    public ResponseEntity<?> updateBrigade(HttpServletRequest req,
                                           @PathVariable Long id,
                                           @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/brigadas/" + id,
            HttpMethod.PUT,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity.status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
 
    @PostMapping("/api/users/brigadas/respond")
    public ResponseEntity<?> brigadeRespond(HttpServletRequest req,
                                            @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/brigadas/respond",
            HttpMethod.POST,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity.status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
 
    @PutMapping("/api/users/brigadas/location")
    public ResponseEntity<?> updateBrigadeLocation(HttpServletRequest req,
                                                   @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/brigadas/location",
            HttpMethod.PUT,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity.status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
 
    @GetMapping("/api/users/brigadas/active")
    public ResponseEntity<?> getActiveBrigades(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            userUrl + "/api/users/brigadas/active",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity.status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }
 
    // ── REPORTS ───────────────────────────────────────────

    @PostMapping("/api/reports")
    public ResponseEntity<?> createReport(HttpServletRequest req,
                                        @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            reportUrl + "/api/reports",
            HttpMethod.POST,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @GetMapping("/api/reports")
    public ResponseEntity<?> getReports(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            reportUrl + "/api/reports",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }


    @PostMapping("/api/notifications/location")
    public ResponseEntity<?> updateLocation(HttpServletRequest req,
                                            @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            notificationUrl + "/api/notifications/location",
            HttpMethod.POST,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @GetMapping("/api/notifications/location/my")
    public ResponseEntity<?> getMyNotifications(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            notificationUrl + "/api/notifications/location/my",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @GetMapping("/api/notifications/admin/alerts")
    public ResponseEntity<?> getAdminAlerts(HttpServletRequest req) {
        ResponseEntity<String> response = restTemplate.exchange(
            notificationUrl + "/api/notifications/admin/alerts",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }

    @PutMapping("/api/notifications/admin/alerts/{id}/review")
    public ResponseEntity<?> reviewAlert(HttpServletRequest req,
                                        @PathVariable Long id,
                                        @RequestBody Map<String, Object> body) {
        ResponseEntity<String> response = restTemplate.exchange(
            notificationUrl + "/api/notifications/admin/alerts/" + id + "/review",
            HttpMethod.PUT,
            new HttpEntity<>(body, userHeaders(req)),
            String.class
        );
        return ResponseEntity
            .status(response.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(response.getBody());
    }




    // ── Health ────────────────────────────────────────────

    @GetMapping("/")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "gateway corriendo", "version", "3.3.0"));
    }

    // ── Helpers ───────────────────────────────────────────

private HttpHeaders jsonHeaders() {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    return h;
    }

    private HttpHeaders userHeaders(HttpServletRequest req) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("x-user-id",  String.valueOf(req.getAttribute("userId")));
        h.set("x-user-role", String.valueOf(req.getAttribute("userRole")));
        h.set("x-user-brigade", String.valueOf(req.getAttribute("brigadaId")));
        h.set("x-internal-key", String.valueOf(req.getAttribute("internalSecret")));
        return h;
    }
}