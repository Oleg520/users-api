package ru.oleg520.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.oleg520.dto.CreateUserRequestDto;
import ru.oleg520.dto.UpdateUserDto;
import ru.oleg520.dto.UserResponseDto;

@RequestMapping(path = "/users")
@Tag(name = "Users", description = "Операции с пользователями")
public interface UserApi {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            operationId = "createUser",
            summary = "Создание User",
            description = "Создает нового пользователя и возвращает ресурс с HATEOAS ссылками",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User создан",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ru.oleg520.dto.UserResponseDto.class)
                            ),
                            links = {
                                    @Link(name = "self", operationId = "getUser",
                                            parameters = @LinkParameter(name = "userId", expression = "$response.body#/id")),
                                    @Link(name = "allUsers", operationId = "getAllUsers"),
                                    @Link(name = "updateUser", operationId = "updateUser",
                                            parameters = @LinkParameter(name = "userId", expression = "$response.body#/id")),
                                    @Link(name = "deleteUser", operationId = "deleteUser",
                                            parameters = @LinkParameter(name = "userId", expression = "$response.body#/id"))
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос"),
                    @ApiResponse(responseCode = "409", description = "User с таким email уже существует")
            }
    )
    ResponseEntity<EntityModel<UserResponseDto>> createUser(
            @RequestBody(description = "Данные нового User", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequestDto.class)))
            @org.springframework.web.bind.annotation.RequestBody CreateUserRequestDto dto);

    @GetMapping("/{userId}")
    @Operation(operationId = "getUser", summary = "Получить User по ID")
    EntityModel<UserResponseDto> getUserById(@PathVariable Long userId);

    @GetMapping
    @Operation(operationId = "getAllUsers", summary = "Получить всех пользователей")
    CollectionModel<EntityModel<UserResponseDto>> getAllUsers();

    @PatchMapping("/{userId}")
    @Operation(operationId = "updateUser", summary = "Обновить User")
    EntityModel<UserResponseDto> updateUser(@PathVariable Long userId,
                                            @org.springframework.web.bind.annotation.RequestBody UpdateUserDto updateUserDto);

    @DeleteMapping("/{userId}")
    @Operation(operationId = "deleteUser", summary = "Удалить User")
    Long deleteUser(@PathVariable Long userId);
}
