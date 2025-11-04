package com.atuantes.mentes.user.application.command;

import com.atuantes.mentes.user.domain.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter @Getter
public class UpdateUserCommand {
    private String document;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private Category category;
    private Boolean active;

    public UpdateUserCommand() {
    }

    public UpdateUserCommand(String document, String fullName, String email, String phone, LocalDate birthdate,
                             Category category, Boolean active) {
        this.document = Objects.requireNonNull(document);
        this.fullName = Objects.requireNonNull(fullName);
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
        this.birthdate = Objects.requireNonNull(birthdate);
        this.category = Objects.requireNonNull(category);
        this.active = Objects.requireNonNull(active);
    }
}
