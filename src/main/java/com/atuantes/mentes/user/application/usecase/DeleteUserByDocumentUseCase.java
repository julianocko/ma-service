package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.command.DeleteUserByDocumentCommand;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.service.UserDelete;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record DeleteUserByDocumentUseCase(UserDelete userDelete) {

    public void execute(DeleteUserByDocumentCommand command, UUID transactionId) {

        log.info(LogMessage.LOG_START_USE_CASE.getMessage(), "delete user", transactionId);

        userDelete.deleteByDocument(command.getDocument(), transactionId);

        log.info(LogMessage.LOG_END_USE_CASE.getMessage(), "delete user", transactionId);
    }
}
