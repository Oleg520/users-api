package ru.oleg520.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import ru.oleg520.dto.user.NewUserDto;
import ru.oleg520.dto.user.UpdateUserDto;
import ru.oleg520.dto.user.UserDto;
import ru.oleg520.dto.user.CreateUserRequestDto;
import ru.oleg520.dto.user.UserResponseDto;
import ru.oleg520.mapper.UserMapper;
import ru.oleg520.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final UserModelAssembler userAssembler;
    private final UserMapper userMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(@RequestBody @Valid CreateUserRequestDto dto) {
        NewUserDto serviceDto = new NewUserDto(dto.name(), dto.email(), dto.age());
        UserDto created = userService.create(serviceDto);
        UserResponseDto responseDto = userMapper.toResponseDto(created);
        EntityModel<UserResponseDto> model = userAssembler.toModel(responseDto);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @GetMapping("/{userId}")
    public EntityModel<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserDto dto = userService.getById(userId);
        return userAssembler.toModel(userMapper.toResponseDto(dto));
    }

    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> getAllUsers() {
        List<EntityModel<UserResponseDto>> users = userService.getAll().stream()
                .map(userMapper::toResponseDto)
                .map(userAssembler::toModel)
                .collect(Collectors.toList());
        Link self = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers()).withSelfRel();
        Link create = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).createUser(null)).withRel("create");
        return CollectionModel.of(users, self, create);
    }

    @PatchMapping("/{userId}")
    public EntityModel<UserResponseDto> updateUser(@PathVariable Long userId,
                                                   @RequestBody UpdateUserDto updateUserDto) {
        UserDto updated = userService.update(userId, updateUserDto);
        return userAssembler.toModel(userMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable Long userId) {
        return userService.delete(userId);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User service is healthy");
    }
}
