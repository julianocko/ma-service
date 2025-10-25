package com.atuantes.mentes.user.presentation.controller;



import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.presentation.dto.CreateUserDto;
import com.atuantes.mentes.user.presentation.mapper.CreateUserDtoToCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser (@RequestHeader("x-transaction-id") UUID transactionId,
                                            @RequestBody @Valid CreateUserDto dto) {
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "create user", transactionId);

        CreateUserCommand command = CreateUserDtoToCommand.toCommand(dto);

        var createdUser = createUserUseCase.createUser(command, transactionId);

        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "create user", transactionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
