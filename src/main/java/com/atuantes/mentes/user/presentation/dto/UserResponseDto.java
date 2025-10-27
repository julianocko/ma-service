package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String fullName,
        boolean active,
        String document,
        String email,
        String phone,
        LocalDate birthdate,
        Category category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
