package ru.oleg520.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateUserRequestDto(
        @NotBlank(message = "Имя не должно быть пустым")
        String name,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @Positive(message = "Возраст должен быть положительным числом")
        @Max(value = 100, message = "Возраст не должен превышать 100 лет")
        Integer age
) {
}
