package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.exception.UserInvalidDocumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateUserCommandToUser {

    public User toUser(CreateUserCommand command) {
        try {
            return new User(
                    command.getFullName(),
                    command.getDocument(),
                    command.getEmail(),
                    command.getPhone(),
                    command.getBirthdate(),
                    command.getCategory()
            );
        }catch (UserInvalidDocumentException e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(), e.getCode(), e.getMessage(), null);
            throw new UserIllegalArgumentException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(),
                    UserErrorMessage.CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR.getCode(), e.getMessage(), null);
            throw new UserIllegalArgumentException(UserErrorMessage.CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR.getCode(),
                    UserErrorMessage.CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR.getMessage());
        }
    }
}
