package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.presentation.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseDto {

    public UserResponseDto map(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.isActive(),
                user.getDocument(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
