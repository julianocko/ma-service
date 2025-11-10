package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.presentation.util.ValidAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class CreateUserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O nome completo é obrigatório")
    private String fullName;

    @NotBlank(message = "O documento é obrigatório")
    private String document;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    private String phone;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    @ValidAge(max = 120)
    private LocalDate birthdate;

    @NotNull(message = "A categoria é obrigatória")
    private Category category;
}
