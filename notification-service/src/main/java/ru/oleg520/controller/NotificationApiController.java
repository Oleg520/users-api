package ru.oleg520.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.oleg520.dto.notification.NotificationRequestDto;
import ru.oleg520.dto.notification.NotificationResponseDto;
import ru.oleg520.service.NotificationApiService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationApiController {

    private final NotificationApiService notificationApiService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDto> sendNotification(@RequestBody NotificationRequestDto request) {
        log.info("Received notification request for user: {}, type: {}", request.userId(), request.type());

        try {
            boolean success = notificationApiService.sendNotification(request);

            NotificationResponseDto response = new NotificationResponseDto(
                success,
                success ? "Notification sent successfully" : "Failed to send notification",
                request.userId()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing notification request", e);
            NotificationResponseDto errorResponse = new NotificationResponseDto(
                false,
                "Internal server error: " + e.getMessage(),
                request.userId()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification service is healthy");
    }
}
