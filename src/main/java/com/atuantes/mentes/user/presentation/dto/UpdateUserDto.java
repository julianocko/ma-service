package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateUserDto(

        @NotBlank(message = "O nome completo é obrigatório")
        String fullName,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String phone,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser no passado")
        LocalDate birthdate,

        @NotNull(message = "A categoria é obrigatória")
        Category category,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean active
) {
}
