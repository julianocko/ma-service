package com.atuantes.mentes.user.presentation.dto;

import com.atuantes.mentes.user.domain.entity.Category;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class UserDto extends RepresentationModel<UserDto> implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDto userDto = (UserDto) o;
        return active == userDto.active && Objects.equals(id, userDto.id) && Objects.equals(fullName, userDto.fullName)
                && Objects.equals(document, userDto.document) && Objects.equals(email, userDto.email)
                && Objects.equals(phone, userDto.phone) && Objects.equals(birthdate, userDto.birthdate)
                && category == userDto.category && Objects.equals(createdAt, userDto.createdAt)
                && Objects.equals(updatedAt, userDto.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, fullName, active, document, email, phone, birthdate, category, createdAt, updatedAt);
    }
}
