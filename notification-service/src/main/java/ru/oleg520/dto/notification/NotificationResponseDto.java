package ru.oleg520.dto.notification;

public record NotificationResponseDto(
    boolean success,
    String message,
    Long userId
) {}
