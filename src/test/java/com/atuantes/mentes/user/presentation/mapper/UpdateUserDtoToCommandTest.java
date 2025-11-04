package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given UpdateUserDtoToCommand")
class UpdateUserDtoToCommandTest {

    private UpdateUserDtoToCommand mapper;
    private String validDocument;
    private UpdateUserDto validDto;

    @BeforeEach
    void setUp() {
        mapper = new UpdateUserDtoToCommand();
        validDocument = "00588380903";
        validDto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );
    }

    @Test
    @DisplayName("When mapping valid dto Then should return UpdateUserCommand with all fields")
    void whenMappingValidDto_thenShouldReturnUpdateUserCommandWithAllFields() {
        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, validDto);

        // Then
        assertNotNull(result);
        assertEquals("00588380903", result.getDocument());
        assertEquals("João Silva", result.getFullName());
        assertEquals("joao@test.com", result.getEmail());
        assertEquals("11999999999", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertEquals(Category.FATHER, result.getCategory());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("When mapping dto with active true Then should return command with active true")
    void whenMappingDtoWithActiveTrue_thenShouldReturnCommandWithActiveTrue() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("When mapping dto with active false Then should return command with active false")
    void whenMappingDtoWithActiveFalse_thenShouldReturnCommandWithActiveFalse() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                false
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertFalse(result.getActive());
    }

    @Test
    @DisplayName("When mapping dto with null active Then should throw UserIllegalArgumentException")
    void whenMappingDtoWithNullActive_thenShouldThrowUserIllegalArgumentException() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                null
        );

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(validDocument, dto);
        });

        assertEquals("USER-UPDATE-001", exception.getCode());
        assertEquals("Erro ao mapear UpdateUserDto para UpdateUserCommand", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When mapping dto with each category Then should map correctly")
    void whenMappingDtoWithEachCategory_thenShouldMapCorrectly(Category category) {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                category,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals(category, result.getCategory());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00588380903", "12345678900", "98765432100"})
    @DisplayName("When mapping with different documents Then should map correctly")
    void whenMappingWithDifferentDocuments_thenShouldMapCorrectly(String document) {
        // When
        UpdateUserCommand result = mapper.toCommand(document, validDto);

        // Then
        assertEquals(document, result.getDocument());
    }

    @Test
    @DisplayName("When mapping dto with special characters in name Then should map correctly")
    void whenMappingDtoWithSpecialCharactersInName_thenShouldMapCorrectly() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "José María Ñoño de Souza",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"joao@test.com", "maria@example.com", "user@domain.com.br"})
    @DisplayName("When mapping dto with different emails Then should map correctly")
    void whenMappingDtoWithDifferentEmails_thenShouldMapCorrectly(String email) {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                email,
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals(email, result.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"11999999999", "11988888888", "21987654321"})
    @DisplayName("When mapping dto with different phones Then should map correctly")
    void whenMappingDtoWithDifferentPhones_thenShouldMapCorrectly(String phone) {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                phone,
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals(phone, result.getPhone());
    }

    @Test
    @DisplayName("When mapping dto with past birthdate Then should map correctly")
    void whenMappingDtoWithPastBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate pastDate = LocalDate.of(1950, 1, 1);
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                pastDate,
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals(pastDate, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping dto with recent birthdate Then should map correctly")
    void whenMappingDtoWithRecentBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate recentDate = LocalDate.of(2020, 12, 31);
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                recentDate,
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(validDocument, dto);

        // Then
        assertEquals(recentDate, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping multiple dtos Then should process each independently")
    void whenMappingMultipleDtos_thenShouldProcessEachIndependently() {
        // Given
        UpdateUserDto dto1 = new UpdateUserDto(
                "User 1",
                "user1@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        UpdateUserDto dto2 = new UpdateUserDto(
                "User 2",
                "user2@test.com",
                "11988888888",
                LocalDate.of(1995, 5, 15),
                Category.MOTHER,
                false
        );

        // When
        UpdateUserCommand result1 = mapper.toCommand("00588380903", dto1);
        UpdateUserCommand result2 = mapper.toCommand("98765432100", dto2);

        // Then
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals("00588380903", result1.getDocument());
        assertEquals("98765432100", result2.getDocument());
        assertTrue(result1.getActive());
        assertFalse(result2.getActive());
    }

    @Test
    @DisplayName("When UpdateUserCommand constructor throws exception Then should throw UserIllegalArgumentException")
    void whenUpdateUserCommandConstructorThrowsException_thenShouldThrowUserIllegalArgumentException() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                null,
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(validDocument, dto);
        });

        assertEquals("USER-UPDATE-001", exception.getCode());
        assertEquals("Erro ao mapear UpdateUserDto para UpdateUserCommand", exception.getMessage());
    }

    @Test
    @DisplayName("When dto is null Then should throw UserIllegalArgumentException")
    void whenDtoIsNull_thenShouldThrowUserIllegalArgumentException() {
        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(validDocument, null);
        });

        assertEquals("USER-UPDATE-001", exception.getCode());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("When document is null Then should throw UserIllegalArgumentException")
    void whenDocumentIsNull_thenShouldThrowUserIllegalArgumentException(String document) {
        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(document, validDto);
        });

        assertEquals("USER-UPDATE-001", exception.getCode());
    }

    @Test
    @DisplayName("When exception occurs Then should log error")
    void whenExceptionOccurs_thenShouldLogError() {
        // Given
        UpdateUserDto dto = new UpdateUserDto(
                null,
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When & Then
        assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(validDocument, dto);
        });
    }
}