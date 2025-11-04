package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateUserDtoToCommand {

    public UpdateUserCommand toCommand(String document, UpdateUserDto dto) {
        try {
            return new UpdateUserCommand(
                    document,
                    dto.fullName(),
                    dto.email(),
                    dto.phone(),
                    dto.birthdate(),
                    dto.category(),
                    dto.active()
            );
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getSimpleName(),
                    "UPDATE-USER-DTO-TO-COMMAND-ERROR", e.getMessage(), null);
            throw new UserIllegalArgumentException("UPDATE-USER-DTO-TO-COMMAND-ERROR",
                    "Error mapping UpdateUserDto to UpdateUserCommand");
        }
    }
}
