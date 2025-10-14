package ru.oleg520.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.oleg520.dto.NewUserEventDto;
import ru.oleg520.dto.UserEvent;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @KafkaListener(
            topics = "user-events",
            groupId = "notification-group"
    )
    public void handleUserEvent(UserEvent event) {
        emailService.send(new NewUserEventDto(event.email(), event.operation()));
    }
}
