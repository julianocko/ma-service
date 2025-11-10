package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import com.atuantes.mentes.user.presentation.dto.UserDto;
import com.atuantes.mentes.user.presentation.mapper.UpdateUserDtoToCommand;
import com.atuantes.mentes.user.presentation.mapper.UserToUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UpdateUserByDocumentController {

    private final UpdateUserDtoToCommand updateUserDtoToCommand;
    private final UpdateUserUseCase updateUserUseCase;

    @PutMapping(
            value = "/document/{document}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUserByDocument(
            @RequestHeader("x-transaction-id") UUID transactionId,
            @PathVariable String document,
            @RequestBody @Valid UpdateUserDto dto) {
        
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "update user by document", transactionId);

        String normalizedDocument = document.replaceAll("\\D", "");
        UpdateUserCommand command = updateUserDtoToCommand.toCommand(normalizedDocument, dto);

        UserDto userDto =  UserToUserDto.toDto(updateUserUseCase.updateUser(command, transactionId));

        var responseDto = addHateoasLinks(transactionId, document, userDto);

        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "update user by document", transactionId);

        return ResponseEntity.ok(responseDto);
    }

    private static UserDto addHateoasLinks(UUID transactionId, String document, UserDto userDto) {
        userDto.add(linkTo(methodOn(UpdateUserByDocumentController.class).updateUserByDocument(transactionId,
                userDto.getDocument(), null)).withSelfRel().withType("PUT"));
        userDto.add(linkTo(methodOn(CreateUserController.class).createUser(transactionId, null))
                .withRel("create").withType("POST"));
        userDto.add(linkTo(methodOn(FindUserByDocumentController.class).findByDocument(transactionId, document))
                .withRel("find").withType("GET"));
        userDto.add(linkTo(methodOn(DeleteUserByDocumentController.class).deleteByDocument(transactionId, document))
                .withRel("delete").withType("DELETE"));

        return userDto;
    }
}
