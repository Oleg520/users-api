package ru.oleg520.dto;

public record EmailSendResponse(
        String to,
        String subject,
        String message
) {
}
