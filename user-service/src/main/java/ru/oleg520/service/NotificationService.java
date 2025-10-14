package ru.oleg520.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.oleg520.dto.notification.NotificationRequestDto;
import ru.oleg520.dto.notification.NotificationType;
import ru.oleg520.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final RestTemplate restTemplate;

    @Value("${app.notification.service-url:http://notification-service}")
    private String notificationServiceUrl;

    @CircuitBreaker(name = "notification-service", fallbackMethod = "fallbackWelcomeNotification")
    @Retry(name = "notification-service")
    public void sendWelcomeNotification(User user) {
        NotificationRequestDto request = new NotificationRequestDto(
            user.getEmail(),
            NotificationType.WELCOME,
            "Добро пожаловать в нашу систему!",
            user.getId()
        );

        String url = notificationServiceUrl + "/api/notifications/send";
        restTemplate.postForObject(url, request, String.class);

        log.info("Welcome notification sent successfully for user: {}", user.getEmail());
    }

    @CircuitBreaker(name = "notification-service", fallbackMethod = "fallbackGoodbyeNotification")
    @Retry(name = "notification-service")
    public void sendGoodbyeNotification(User user) {
        NotificationRequestDto request = new NotificationRequestDto(
            user.getEmail(),
            NotificationType.GOODBYE,
            "Спасибо за использование наших услуг!",
            user.getId()
        );

        String url = notificationServiceUrl + "/api/notifications/send";
        restTemplate.postForObject(url, request, String.class);

        log.info("Goodbye notification sent successfully for user: {}", user.getEmail());
    }

    public void fallbackWelcomeNotification(User user, Exception ex) {
        log.warn("Fallback: Welcome notification failed for user: {}. Reason: {}",
                user.getEmail(), ex.getMessage());
    }

    public void fallbackGoodbyeNotification(User user, Exception ex) {
        log.warn("Fallback: Goodbye notification failed for user: {}. Reason: {}",
                user.getEmail(), ex.getMessage());
    }
}
