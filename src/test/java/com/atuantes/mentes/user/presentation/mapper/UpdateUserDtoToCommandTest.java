package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given UpdateUserDtoToCommand")
class UpdateUserDtoToCommandTest {

    private UpdateUserDtoToCommand mapper;
    private String document;
    private UpdateUserDto validDto;

    @BeforeEach
    void setUp() {
        mapper = new UpdateUserDtoToCommand();
        document = "00588380903";
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
    @DisplayName("When mapping valid DTO Then should return UpdateUserCommand")
    void whenMappingValidDto_thenShouldReturnUpdateUserCommand() {
        // When
        UpdateUserCommand result = mapper.toCommand(document, validDto);

        // Then
        assertNotNull(result);
        assertEquals(document, result.getDocument());
        assertEquals("João Silva", result.getFullName());
        assertEquals("joao@test.com", result.getEmail());
        assertEquals("11999999999", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertEquals(Category.FATHER, result.getCategory());
        assertTrue(result.getActive());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When mapping DTO with each category Then should map correctly")
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
    }

    @Test
    @DisplayName("When mapping DTO with inactive status Then should map correctly")
    void whenMappingDtoWithInactiveStatus_thenShouldMapCorrectly() {
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertNotNull(result);
        assertFalse(result.getActive());
    }

    @Test
    @DisplayName("When mapping DTO with special characters in name Then should map correctly")
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @Test
    @DisplayName("When mapping DTO with different documents Then should use correct document")
    void whenMappingDtoWithDifferentDocuments_thenShouldUseCorrectDocument() {
        // Given
        String document1 = "00588380903";
        String document2 = "98765432100";

        // When
        UpdateUserCommand result1 = mapper.toCommand(document1, validDto);
        UpdateUserCommand result2 = mapper.toCommand(document2, validDto);

        // Then
        assertEquals(document1, result1.getDocument());
        assertEquals(document2, result2.getDocument());
    }

    @Test
    @DisplayName("When mapping DTO with birthdate today Then should map correctly")
    void whenMappingDtoWithBirthdateToday_thenShouldMapCorrectly() {
        // Given
        LocalDate today = LocalDate.now();
        UpdateUserDto dto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                today,
                Category.FATHER,
                true
        );

        // When
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertEquals(today, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping DTO with past birthdate Then should map correctly")
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertEquals(pastDate, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping multiple DTOs Then should process each independently")
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
        UpdateUserCommand result1 = mapper.toCommand(document, dto1);
        UpdateUserCommand result2 = mapper.toCommand(document, dto2);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals("user1@test.com", result1.getEmail());
        assertEquals("user2@test.com", result2.getEmail());
        assertTrue(result1.getActive());
        assertFalse(result2.getActive());
    }

    @Test
    @DisplayName("When mapping DTO with all fields filled Then should map all fields correctly")
    void whenMappingDtoWithAllFieldsFilled_thenShouldMapAllFieldsCorrectly() {
        // When
        UpdateUserCommand result = mapper.toCommand(document, validDto);

        // Then
        assertNotNull(result.getDocument());
        assertNotNull(result.getFullName());
        assertNotNull(result.getEmail());
        assertNotNull(result.getPhone());
        assertNotNull(result.getBirthdate());
        assertNotNull(result.getCategory());
        assertNotNull(result.getActive());
    }

    @ParameterizedTest
    @ValueSource(strings = {"11999999999", "11988888888", "21987654321"})
    @DisplayName("When mapping DTO with different phones Then should map correctly")
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertEquals(phone, result.getPhone());
    }

    @ParameterizedTest
    @ValueSource(strings = {"joao@test.com", "maria@example.com", "user@domain.com.br"})
    @DisplayName("When mapping DTO with different emails Then should map correctly")
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
        UpdateUserCommand result = mapper.toCommand(document, dto);

        // Then
        assertEquals(email, result.getEmail());
    }
}