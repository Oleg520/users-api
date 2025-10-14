package ru.oleg520.dto.user;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Integer age
) {
}
