package ru.oleg520.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.oleg520.dto.notification.NotificationRequestDto;
import ru.oleg520.dto.notification.NotificationType;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class ServiceDiscoveryTestController {

    private final RestTemplate restTemplate;

    @Value("${app.notification.service-url:http://notification-service}")
    private String notificationServiceUrl;

    @GetMapping("/notification-health")
    public ResponseEntity<String> testNotificationServiceHealth() {
        try {
            String url = notificationServiceUrl + "/api/notifications/health";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Notification service health check successful: {}", response);
            return ResponseEntity.ok("Notification service is reachable: " + response);
        } catch (Exception e) {
            log.error("Failed to reach notification service", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to reach notification service: " + e.getMessage());
        }
    }

    @PostMapping("/send-test-notification")
    public ResponseEntity<String> sendTestNotification(@RequestParam String email) {
        try {
            NotificationRequestDto request = new NotificationRequestDto(
                email,
                NotificationType.WELCOME,
                "Test notification from Service Discovery",
                999L
            );

            String url = notificationServiceUrl + "/api/notifications/send";
            String response = restTemplate.postForObject(url, request, String.class);

            log.info("Test notification sent successfully: {}", response);
            return ResponseEntity.ok("Test notification sent: " + response);
        } catch (Exception e) {
            log.error("Failed to send test notification", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to send test notification: " + e.getMessage());
        }
    }
}
