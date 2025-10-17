package ru.oleg520.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.oleg520.dto.EmailSendResponse;
import ru.oleg520.dto.NewUserEventDto;
import ru.oleg520.dto.notification.NotificationRequestDto;
import ru.oleg520.dto.event.UserOperationType;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationApiService {

    private final EmailService emailService;

    @CircuitBreaker(name = "email-service", fallbackMethod = "fallbackSendNotification")
    @Retry(name = "email-service")
    public boolean sendNotification(NotificationRequestDto request) {
        UserOperationType operation;

        switch (request.type()) {
            case WELCOME:
                operation = UserOperationType.CREATE;
                break;
            case GOODBYE:
                operation = UserOperationType.DELETE;
                break;
            default:
                operation = UserOperationType.CREATE;
                break;
        }

        NewUserEventDto eventDto = new NewUserEventDto(request.email(), operation);
        EmailSendResponse response = emailService.send(eventDto);

        log.info("Notification sent successfully via HTTP API - Type: {}, User: {}, Response: {}",
                request.type(), request.userId(), response);

        return true;
    }

    public boolean fallbackSendNotification(NotificationRequestDto request, Exception ex) {
        log.warn("Fallback: Email notification failed for user: {}, type: {}. Reason: {}",
                request.userId(), request.type(), ex.getMessage());
        return false;
    }
}
