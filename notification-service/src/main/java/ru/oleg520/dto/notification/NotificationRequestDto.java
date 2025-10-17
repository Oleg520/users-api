package ru.oleg520.dto.notification;

public record NotificationRequestDto(
    String email,
    NotificationType type,
    String message,
    Long userId
) {}
