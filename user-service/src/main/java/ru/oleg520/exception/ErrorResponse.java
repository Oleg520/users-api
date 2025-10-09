package ru.oleg520.exception;

public record ErrorResponse(
        String path,
        int statusCode,
        String error,
        String message
) {
}
