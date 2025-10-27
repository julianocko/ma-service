package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.message.LogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FindUserByDocumentController {

    private final FindUserByDocumentUseCase findUserByDocumentUseCase;

    @GetMapping(value = "/document/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findByDocument(@RequestHeader("x-transaction-id") UUID transactionId,
                                               @PathVariable String document) {
        log.info(LogMessage.LOG_START_CONTROLLER.getMessage(), "find user by document", transactionId);
        String normalizedDocument = document.replaceAll("\\D", "");
        User user = findUserByDocumentUseCase.findUserByDocument(normalizedDocument, transactionId);
        log.info(LogMessage.LOG_END_CONTROLLER.getMessage(), "find user by document", transactionId);
        return ResponseEntity.ok(user);
    }
}
