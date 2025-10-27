package com.atuantes.mentes.user.application.command;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.service.DocumentValidationService;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;



@Setter @Getter
public class CreateUserCommand {
    private String fullName;
    private String document;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private Category category;

    public CreateUserCommand() {
    }

    public CreateUserCommand(String fullName, String document, String email, String phone, LocalDate birthdate,
                             Category category) {
        this.fullName = Objects.requireNonNull(fullName);
        this.document = Objects.requireNonNull(document);
        documentValidation();
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
        this.birthdate = Objects.requireNonNull(birthdate);
        this.category = Objects.requireNonNull(category);
    }

    private void documentValidation() {
        DocumentValidationService.documentValidation(this.document);
    }
}
