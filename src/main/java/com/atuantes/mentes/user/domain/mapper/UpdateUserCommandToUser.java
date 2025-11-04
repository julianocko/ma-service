package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserCommandToUser {

    public User toUser(UpdateUserCommand command) {
        try {
            User user = new User(
                    command.getFullName(),
                    command.getDocument(),
                    command.getEmail(),
                    command.getPhone(),
                    command.getBirthdate(),
                    command.getCategory()
            );

            if (command.getActive() != null && !command.getActive()) {
                user.setActive(false);
            }

            return user;
        } catch (Exception e) {
            throw new UserIllegalArgumentException("USER-UPDATE-002", "Erro ao mapear UpdateUserCommand para User");
        }
    }
}