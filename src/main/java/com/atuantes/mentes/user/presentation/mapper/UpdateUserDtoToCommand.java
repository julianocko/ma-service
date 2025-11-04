package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
                    "USER-UPDATE-001", e.getMessage(), null);
            throw new UserIllegalArgumentException("USER-UPDATE-001",
                    "Erro ao mapear UpdateUserDto para UpdateUserCommand");
        }
    }
}
