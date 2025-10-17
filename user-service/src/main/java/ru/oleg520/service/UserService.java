package ru.oleg520.service;

import ru.oleg520.dto.user.NewUserDto;
import ru.oleg520.dto.user.UpdateUserDto;
import ru.oleg520.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserDto newUserDto);

    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto update(Long userId, UpdateUserDto newUser);

    Long delete(Long id);
}
