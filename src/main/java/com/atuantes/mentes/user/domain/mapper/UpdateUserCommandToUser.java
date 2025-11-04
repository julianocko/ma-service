package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateUserCommandToUser {

    public User toUser(UpdateUserCommand command) {
        try {
            User user = new User();
            user.setDocument(command.getDocument());
            user.setFullName(command.getFullName());
            user.setEmail(command.getEmail());
            user.setPhone(command.getPhone());
            user.setBirthdate(command.getBirthdate());
            user.setCategory(command.getCategory());
            user.setActive(command.getActive() != null ? command.getActive() : true);
            return user;
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(),
                    "USER-UPDATE-002", e.getMessage(), null);
            throw new UserIllegalArgumentException("USER-UPDATE-002",
                    "Erro ao mapear UpdateUserCommand para User");
        }
    }
}
