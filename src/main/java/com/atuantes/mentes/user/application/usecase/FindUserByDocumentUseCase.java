package com.atuantes.mentes.user.application.usecase;


import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record FindUserByDocumentUseCase(FindUserByDocument findUserByDocument) {

    public User findUserByDocument(String document, UUID transactionId) {

        log.info(LogMessage.LOG_START_USE_CASE.getMessage(), "find user by document", transactionId);

        var user = findUserByDocument.execute(document, transactionId);

        log.info(LogMessage.LOG_END_USE_CASE.getMessage(), "find user by document", transactionId);

        return user;
    }
}
