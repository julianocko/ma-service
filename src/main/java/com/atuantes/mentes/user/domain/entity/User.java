package com.atuantes.mentes.user.domain.entity;

import com.atuantes.mentes.user.domain.service.DocumentValidationService;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Setter @Getter
public class User {
    private UUID id;
    private String fullName;
    private boolean active;
    private String document;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String fullName, String document, String email, String phone, LocalDate birthdate, Category category) {
        this.fullName = Objects.requireNonNull(fullName);
        this.document = Objects.requireNonNull(document);
        documentValidation();
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
        this.birthdate = Objects.requireNonNull(birthdate);
        this.category = Objects.requireNonNull(category);
        this.active = Boolean.TRUE;
    }

    private void documentValidation() {
        DocumentValidationService.documentValidation(this.document);
    }
}
