package ru.oleg520.dto;

public record UserEvent(
        String email,
        UserOperationType operation
) {
}
