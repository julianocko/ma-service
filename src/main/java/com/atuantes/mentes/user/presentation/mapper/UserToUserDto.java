package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.presentation.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserToUserDto {

    public static UserDto toDto(User user) {
        try {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setActive(user.isActive());
            dto.setDocument(user.getDocument());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setBirthdate(user.getBirthdate());
            dto.setCategory(user.getCategory());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setUpdatedAt(user.getUpdatedAt());
            return dto;
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getName(), e.getMessage());
            throw new UserIllegalArgumentException(UserErrorMessage.USER_TO_USER_DTO_MAPPER_ERROR.getCode(),
                    UserErrorMessage.USER_TO_USER_DTO_MAPPER_ERROR.getMessage());
        }
    }
}