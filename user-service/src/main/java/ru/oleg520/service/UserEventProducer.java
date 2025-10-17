package ru.oleg520.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.oleg520.dto.event.UserEvent;

@Service
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    private static final String USER_EVENTS_TOPIC = "user-events";

    public void sendUserEvent(UserEvent event) {
        kafkaTemplate.send(USER_EVENTS_TOPIC, event);
    }
}
