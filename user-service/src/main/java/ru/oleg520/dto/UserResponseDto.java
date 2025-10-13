package ru.oleg520.dto;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Integer age
) {
}
