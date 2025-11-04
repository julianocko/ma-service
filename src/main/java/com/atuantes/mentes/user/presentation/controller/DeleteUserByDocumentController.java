package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.DeleteUserByDocumentCommand;
import com.atuantes.mentes.user.application.usecase.DeleteUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.message.LogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class DeleteUserByDocumentController {

    private final DeleteUserByDocumentUseCase deleteUserByDocumentUseCase;

    @DeleteMapping("/document/{document}")
    public ResponseEntity<Void> deleteByDocument(@RequestHeader("x-transaction-id") UUID transactionId,
                                                  @PathVariable String document) {
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "delete user by document", transactionId);
        
        String normalizedDocument = document.replaceAll("\\D", "");
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(normalizedDocument);
        deleteUserByDocumentUseCase.execute(command, transactionId);
        
        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "delete user by document", transactionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
