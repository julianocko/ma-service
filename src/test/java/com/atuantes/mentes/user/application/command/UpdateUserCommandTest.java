package com.atuantes.mentes.user.application.command;

import com.atuantes.mentes.user.domain.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given UpdateUserCommand")
class UpdateUserCommandTest {

    @Test
    @DisplayName("When creating with empty constructor Then should initialize with null values")
    void whenCreatingWithEmptyConstructor_thenShouldInitializeWithNullValues() {
        // When
        UpdateUserCommand command = new UpdateUserCommand();

        // Then
        assertNotNull(command);
        assertNull(command.getDocument());
        assertNull(command.getFullName());
        assertNull(command.getEmail());
        assertNull(command.getPhone());
        assertNull(command.getBirthdate());
        assertNull(command.getCategory());
        assertNull(command.getActive());
    }

    @Test
    @DisplayName("When creating with all parameters constructor Then should initialize all fields")
    void whenCreatingWithAllParametersConstructor_thenShouldInitializeAllFields() {
        // Given
        String document = "00588380903";
        String fullName = "João Silva";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;
        Boolean active = true;

        // When
        UpdateUserCommand command = new UpdateUserCommand(
                document, fullName, email, phone, birthdate, category, active
        );

        // Then
        assertNotNull(command);
        assertEquals(document, command.getDocument());
        assertEquals(fullName, command.getFullName());
        assertEquals(email, command.getEmail());
        assertEquals(phone, command.getPhone());
        assertEquals(birthdate, command.getBirthdate());
        assertEquals(category, command.getCategory());
        assertEquals(active, command.getActive());
    }

    @Test
    @DisplayName("When setting document Then should update document")
    void whenSettingDocument_thenShouldUpdateDocument() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        String document = "00588380903";

        // When
        command.setDocument(document);

        // Then
        assertEquals(document, command.getDocument());
    }

    @Test
    @DisplayName("When setting fullName Then should update fullName")
    void whenSettingFullName_thenShouldUpdateFullName() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        String fullName = "João Silva";

        // When
        command.setFullName(fullName);

        // Then
        assertEquals(fullName, command.getFullName());
    }

    @Test
    @DisplayName("When setting email Then should update email")
    void whenSettingEmail_thenShouldUpdateEmail() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        String email = "joao@test.com";

        // When
        command.setEmail(email);

        // Then
        assertEquals(email, command.getEmail());
    }

    @Test
    @DisplayName("When setting phone Then should update phone")
    void whenSettingPhone_thenShouldUpdatePhone() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        String phone = "11999999999";

        // When
        command.setPhone(phone);

        // Then
        assertEquals(phone, command.getPhone());
    }

    @Test
    @DisplayName("When setting birthdate Then should update birthdate")
    void whenSettingBirthdate_thenShouldUpdateBirthdate() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        LocalDate birthdate = LocalDate.of(1990, 1, 1);

        // When
        command.setBirthdate(birthdate);

        // Then
        assertEquals(birthdate, command.getBirthdate());
    }

    @Test
    @DisplayName("When setting category Then should update category")
    void whenSettingCategory_thenShouldUpdateCategory() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        Category category = Category.FATHER;

        // When
        command.setCategory(category);

        // Then
        assertEquals(category, command.getCategory());
    }

    @Test
    @DisplayName("When setting active Then should update active")
    void whenSettingActive_thenShouldUpdateActive() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        Boolean active = true;

        // When
        command.setActive(active);

        // Then
        assertEquals(active, command.getActive());
    }

    @Test
    @DisplayName("When creating with null document Then should throw NullPointerException")
    void whenCreatingWithNullDocument_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    null,
                    "João Silva",
                    "joao@test.com",
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null fullName Then should throw NullPointerException")
    void whenCreatingWithNullFullName_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    null,
                    "joao@test.com",
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null email Then should throw NullPointerException")
    void whenCreatingWithNullEmail_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    "João Silva",
                    null,
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null phone Then should throw NullPointerException")
    void whenCreatingWithNullPhone_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    "João Silva",
                    "joao@test.com",
                    null,
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null birthdate Then should throw NullPointerException")
    void whenCreatingWithNullBirthdate_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    "João Silva",
                    "joao@test.com",
                    "11999999999",
                    null,
                    Category.FATHER,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null category Then should throw NullPointerException")
    void whenCreatingWithNullCategory_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    "João Silva",
                    "joao@test.com",
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    null,
                    true
            );
        });
    }

    @Test
    @DisplayName("When creating with null active Then should throw NullPointerException")
    void whenCreatingWithNullActive_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new UpdateUserCommand(
                    "00588380903",
                    "João Silva",
                    "joao@test.com",
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER,
                    null
            );
        });
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When creating with each category Then should set category correctly")
    void whenCreatingWithEachCategory_thenShouldSetCategoryCorrectly(Category category) {
        // When
        UpdateUserCommand command = new UpdateUserCommand(
                "00588380903",
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                category,
                true
        );

        // Then
        assertEquals(category, command.getCategory());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("When creating with active status Then should set active correctly")
    void whenCreatingWithActiveStatus_thenShouldSetActiveCorrectly(Boolean active) {
        // When
        UpdateUserCommand command = new UpdateUserCommand(
                "00588380903",
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                active
        );

        // Then
        assertEquals(active, command.getActive());
    }

    @Test
    @DisplayName("When setting all fields via setters Then should update all fields")
    void whenSettingAllFieldsViaSetters_thenShouldUpdateAllFields() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();

        // When
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // Then
        assertEquals("00588380903", command.getDocument());
        assertEquals("João Silva", command.getFullName());
        assertEquals("joao@test.com", command.getEmail());
        assertEquals("11999999999", command.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), command.getBirthdate());
        assertEquals(Category.FATHER, command.getCategory());
        assertTrue(command.getActive());
    }

    @Test
    @DisplayName("When creating with special characters in name Then should set correctly")
    void whenCreatingWithSpecialCharactersInName_thenShouldSetCorrectly() {
        // When
        UpdateUserCommand command = new UpdateUserCommand(
                "00588380903",
                "José María Ñoño de Souza",
                "jose@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // Then
        assertEquals("José María Ñoño de Souza", command.getFullName());
    }

    @Test
    @DisplayName("When updating active from true to false Then should update correctly")
    void whenUpdatingActiveFromTrueToFalse_thenShouldUpdateCorrectly() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setActive(true);

        // When
        command.setActive(false);

        // Then
        assertFalse(command.getActive());
    }

    @Test
    @DisplayName("When setting null via setter Then should allow null")
    void whenSettingNullViaSetter_thenShouldAllowNull() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();

        // When
        command.setDocument(null);
        command.setFullName(null);
        command.setEmail(null);
        command.setPhone(null);
        command.setBirthdate(null);
        command.setCategory(null);
        command.setActive(null);

        // Then
        assertNull(command.getDocument());
        assertNull(command.getFullName());
        assertNull(command.getEmail());
        assertNull(command.getPhone());
        assertNull(command.getBirthdate());
        assertNull(command.getCategory());
        assertNull(command.getActive());
    }
}