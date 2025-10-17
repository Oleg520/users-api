package ru.oleg520.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import ru.oleg520.dto.user.UserResponseDto;

@Component
@RequiredArgsConstructor
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {
    @Override
    public @NonNull EntityModel<UserResponseDto> toModel(@NonNull UserResponseDto dto) {
        Link self = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(dto.id()))
                .withSelfRel();
        Link collection = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers())
                .withRel("users");
        Link update = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).updateUser(dto.id(), null))
                .withRel("update");
        Link delete = WebMvcLinkBuilder
                .linkTo(UserController.class)
                .slash(dto.id())
                .withRel("delete");
        return EntityModel.of(dto, self, collection, update, delete);
    }
}
