package ru.oleg520.service;

import ru.oleg520.dto.NewUserDto;
import ru.oleg520.dto.UpdateUserDto;
import ru.oleg520.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserDto newUserDto);

    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto update(Long userId, UpdateUserDto newUser);

    void delete(Long id);
}
