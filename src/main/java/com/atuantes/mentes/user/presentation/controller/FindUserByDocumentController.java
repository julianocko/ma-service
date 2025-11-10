package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.presentation.dto.UserDto;
import com.atuantes.mentes.user.presentation.mapper.UserToUserDto;
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
public class FindUserByDocumentController {

    private final FindUserByDocumentUseCase findUserByDocumentUseCase;

    @GetMapping(value = "/document/{document}", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE}
    )
    public ResponseEntity<UserDto> findByDocument(@RequestHeader("x-transaction-id") UUID transactionId,
                                                  @PathVariable String document) {
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "find user by document", transactionId);
        String normalizedDocument = document.replaceAll("\\D", "");
        UserDto userDto = UserToUserDto.toDto(findUserByDocumentUseCase.findUserByDocument(normalizedDocument,
                transactionId));
        var responseDto = addHateoasLinks(transactionId, document, userDto);
        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "find user by document", transactionId);
        return ResponseEntity.ok(responseDto);
    }

    private static UserDto addHateoasLinks(UUID transactionId, String document, UserDto userDto) {
        userDto.add(linkTo(methodOn(FindUserByDocumentController.class).findByDocument(transactionId, document))
                .withSelfRel().withType("GET"));
        userDto.add(linkTo(methodOn(DeleteUserByDocumentController.class).deleteByDocument(transactionId, document))
                .withRel("delete").withType("DELETE"));
        userDto.add(linkTo(methodOn(CreateUserController.class).createUser(transactionId, null))
                .withRel("create").withType("POST"));
        userDto.add(linkTo(methodOn(UpdateUserByDocumentController.class).updateUserByDocument(transactionId,
                userDto.getDocument(), null)).withRel("update").withType("PUT"));
        return userDto;
    }
}
