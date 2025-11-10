package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.presentation.dto.CreateUserDto;
import com.atuantes.mentes.user.presentation.dto.UserDto;
import com.atuantes.mentes.user.presentation.mapper.CreateUserDtoToCommand;
import com.atuantes.mentes.user.presentation.mapper.UserToUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class CreateUserController {

    private final CreateUserDtoToCommand createUserDtoToCommand;

    private final CreateUserUseCase createUserUseCase;

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser (@RequestHeader("x-transaction-id") UUID transactionId,
                                            @RequestBody @Valid CreateUserDto dto) {
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "create user", transactionId);

        CreateUserCommand command = createUserDtoToCommand.toCommand(dto);

        UserDto userDto =  UserToUserDto.toDto(createUserUseCase.createUser(command, transactionId));

        var responseDto = addHateoasLinks(transactionId, dto.getDocument(), userDto, dto);

        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "create user", transactionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    private static UserDto addHateoasLinks(UUID transactionId, String document, UserDto userDto, CreateUserDto dto) {
        userDto.add(linkTo(methodOn(CreateUserController.class).createUser(transactionId, dto))
                .withSelfRel().withType("POST"));
        userDto.add(linkTo(methodOn(FindUserByDocumentController.class).findByDocument(transactionId, document))
                .withRel("find").withType("GET"));
        userDto.add(linkTo(methodOn(DeleteUserByDocumentController.class).deleteByDocument(transactionId, document))
                .withRel("delete").withType("DELETE"));
        userDto.add(linkTo(methodOn(UpdateUserByDocumentController.class).updateUserByDocument(transactionId,
                userDto.getDocument(), null)).withRel("update").withType("PUT"));
        return userDto;
    }
}
