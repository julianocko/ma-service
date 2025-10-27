package com.atuantes.mentes.user.application.usecase;


import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.mapper.CreateUserCommandToUser;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.service.UserInsertRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record CreateUserUseCase(CreateUserCommandToUser createUserCommandToUser,
                                UserInsertRepository userInsertRepository) {

    public User createUser(CreateUserCommand command, UUID transactionId) {

        log.info(LogMessage.LOG_START_USE_CASE.getMessage(), "create user", transactionId);

        User user = createUserCommandToUser.toUser(command);

        var createdUser = userInsertRepository.insert(user, transactionId);

        log.info(LogMessage.LOG_END_USE_CASE.getMessage(), "create user", transactionId);

        return createdUser;
    }
}
