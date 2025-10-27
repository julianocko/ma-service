package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.presentation.util.ValidAge;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserDto(

        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        @NotBlank(message = "O documento é obrigatório")
        String document,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String phone,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser no passado")
        @ValidAge(max = 120)
        LocalDate birthdate,

        @NotNull(message = "A categoria é obrigatória")
        Category category
) {
}
