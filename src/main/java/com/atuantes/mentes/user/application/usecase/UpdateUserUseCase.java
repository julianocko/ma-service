package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.mapper.UpdateUserCommandToUser;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.service.UserUpdate;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record UpdateUserUseCase(UpdateUserCommandToUser updateUserCommandToUser,
                                UserUpdate userUpdate) {

    public User updateUser(UpdateUserCommand command, UUID transactionId) {

        log.info(LogMessage.LOG_START_USE_CASE.getMessage(), "update user", transactionId);

        User user = updateUserCommandToUser.toUser(command);

        var updatedUser = userUpdate.update(user, transactionId);

        log.info(LogMessage.LOG_END_USE_CASE.getMessage(), "update user", transactionId);

        return updatedUser;
    }
}
