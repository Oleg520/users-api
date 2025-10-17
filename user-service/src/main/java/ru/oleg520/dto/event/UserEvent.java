package ru.oleg520.dto.event;

public record UserEvent(
        String email,
        UserOperationType operation
) {
}
