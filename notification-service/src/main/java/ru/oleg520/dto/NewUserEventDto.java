package ru.oleg520.dto;

import jakarta.validation.constraints.Email;
import ru.oleg520.dto.event.UserOperationType;

public record NewUserEventDto(
        @Email(message = "Invalid email format")
        String email,

        UserOperationType operation
) {
}
