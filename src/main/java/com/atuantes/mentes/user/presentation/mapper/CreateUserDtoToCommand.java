package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.exception.UserInvalidDocumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.presentation.dto.CreateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateUserDtoToCommand {

    public CreateUserCommand toCommand(CreateUserDto dto) {
        try {
            return new CreateUserCommand(
                    dto.fullName(),
                    dto.document(),
                    dto.email(),
                    dto.phone(),
                    dto.birthdate(),
                    dto.category()
            );
        } catch (UserInvalidDocumentException e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(), e.getCode(), e.getMessage(), null);
            throw new UserIllegalArgumentException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(),
                    UserErrorMessage.CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR.getCode(), e.getMessage(), null);
            throw new UserIllegalArgumentException(UserErrorMessage.CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR.getCode(),
                    UserErrorMessage.CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR.getMessage());
        }
    }
}
