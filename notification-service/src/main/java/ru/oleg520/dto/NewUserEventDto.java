package ru.oleg520.dto;

import jakarta.validation.constraints.Email;

public record NewUserEventDto(
        @Email(message = "Invalid email format")
        String email,

        UserOperationType operation
) {
}
