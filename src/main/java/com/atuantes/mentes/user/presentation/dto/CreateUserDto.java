package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateUserDto(

        @NotEmpty(message = "O nome completo é obrigatório")
        String fullName,

        @NotEmpty(message = "O documento é obrigatório")
        String document,

        @NotEmpty(message = "O email é obrigatório")
        String email,

        @NotEmpty(message = "O telefone é obrigatório")
        String phone,

        @NotNull(message = "A data de nascimento é obrigatória")
        LocalDate birthdate,

        @NotNull(message = "A categoria é obrigatória")
        Category category
) {
}
