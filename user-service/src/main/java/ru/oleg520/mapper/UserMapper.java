package ru.oleg520.mapper;

import org.mapstruct.*;
import ru.oleg520.dto.user.NewUserDto;
import ru.oleg520.dto.user.UpdateUserDto;
import ru.oleg520.dto.user.UserDto;
import ru.oleg520.dto.user.CreateUserRequestDto;
import ru.oleg520.dto.user.UserResponseDto;
import ru.oleg520.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    UserResponseDto toResponseDto(User user);

    UserResponseDto toResponseDto(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User fromNewUserDto(NewUserDto newUserDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(@MappingTarget User user, UpdateUserDto updateUserDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User fromCreateRequest(CreateUserRequestDto dto);
}
