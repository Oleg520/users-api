package ru.oleg520.service;

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

    public boolean sendNotification(NotificationRequestDto request) {
        try {
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
        } catch (Exception e) {
            log.error("Failed to send notification via HTTP API - Type: {}, User: {}", 
                     request.type(), request.userId(), e);
            return false;
        }
    }
}
