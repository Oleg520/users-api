package ru.oleg520.dto.user;

public record UserDto(
    Long id,
    String name,
    String email,
    Integer age
) {}
