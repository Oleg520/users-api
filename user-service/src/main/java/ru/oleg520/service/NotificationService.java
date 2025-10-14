package ru.oleg520.service;

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

    public void sendWelcomeNotification(User user) {
        try {
            NotificationRequestDto request = new NotificationRequestDto(
                user.getEmail(),
                NotificationType.WELCOME,
                "Добро пожаловать в нашу систему!",
                user.getId()
            );
            
            String url = notificationServiceUrl + "/api/notifications/send";
            restTemplate.postForObject(url, request, String.class);
            
            log.info("Welcome notification sent successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome notification for user: {}", user.getEmail(), e);
        }
    }

    public void sendGoodbyeNotification(User user) {
        try {
            NotificationRequestDto request = new NotificationRequestDto(
                user.getEmail(),
                NotificationType.GOODBYE,
                "Спасибо за использование наших услуг!",
                user.getId()
            );
            
            String url = notificationServiceUrl + "/api/notifications/send";
            restTemplate.postForObject(url, request, String.class);
            
            log.info("Goodbye notification sent successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send goodbye notification for user: {}", user.getEmail(), e);
        }
    }
}
