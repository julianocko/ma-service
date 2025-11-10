package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import com.atuantes.mentes.user.presentation.mapper.UpdateUserDtoToCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UpdateUserByDocumentController {

    private final UpdateUserDtoToCommand updateUserDtoToCommand;
    private final UpdateUserUseCase updateUserUseCase;

    @PutMapping(
            value = "/document/{document}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserByDocument(
            @RequestHeader("x-transaction-id") UUID transactionId,
            @PathVariable String document,
            @RequestBody @Valid UpdateUserDto dto) {
        
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "update user by document", transactionId);

        String normalizedDocument = document.replaceAll("\\D", "");
        UpdateUserCommand command = updateUserDtoToCommand.toCommand(normalizedDocument, dto);

        var updatedUser = updateUserUseCase.updateUser(command, transactionId);

        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "update user by document", transactionId);

        return ResponseEntity.ok(updatedUser);
    }
}
