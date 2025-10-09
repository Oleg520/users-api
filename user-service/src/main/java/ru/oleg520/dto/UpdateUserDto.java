package ru.oleg520.dto;

public record UpdateUserDto(
        String name,
        String email,
        Integer age
) {
}
