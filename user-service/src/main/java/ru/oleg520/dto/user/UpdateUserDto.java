package ru.oleg520.dto.user;

public record UpdateUserDto(
        String name,
        String email,
        Integer age
) {
}
