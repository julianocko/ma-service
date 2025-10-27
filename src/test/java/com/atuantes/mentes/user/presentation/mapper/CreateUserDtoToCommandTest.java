package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.presentation.dto.CreateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given CreateUserDtoToCommand mapper")
class CreateUserDtoToCommandTest {

    private CreateUserDtoToCommand mapper;

    @BeforeEach
    void setUp() {
        mapper = new CreateUserDtoToCommand();
    }

    @Test
    @DisplayName("When converting valid DTO with FATHER category Then should map all fields correctly")
    void whenConvertingValidDtoWithFatherCategory_thenShouldMapAllFieldsCorrectly() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertNotNull(command);
        assertEquals("João Silva", command.getFullName());
        assertEquals("00588380903", command.getDocument());
        assertEquals("joao@test.com", command.getEmail());
        assertEquals("11999999999", command.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), command.getBirthdate());
        assertEquals(Category.FATHER, command.getCategory());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When converting DTO with each category Then should map correctly")
    void whenConvertingDtoWithEachCategory_thenShouldMapCorrectly(Category category) {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "Test User",
                "00588380903",
                "test@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                category
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertNotNull(command);
        assertEquals(category, command.getCategory());
        assertEquals("Test User", command.getFullName());
    }

    @Test
    @DisplayName("When converting DTO with special characters in name Then should preserve them")
    void whenConvertingDtoWithSpecialCharactersInName_thenShouldPreserveThem() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "José María Ñoño de Souza",
                "00588380903",
                "jose@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertEquals("José María Ñoño de Souza", command.getFullName());
    }

    @Test
    @DisplayName("When converting DTO with phone with country code Then should preserve format")
    void whenConvertingDtoWithPhoneWithCountryCode_thenShouldPreserveFormat() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "+5511999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertEquals("+5511999999999", command.getPhone());
    }

    @Test
    @DisplayName("When converting DTO with email in uppercase Then should preserve case")
    void whenConvertingDtoWithEmailInUppercase_thenShouldPreserveCase() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "JOAO@TEST.COM",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertEquals("JOAO@TEST.COM", command.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678900",
            "00000000000",
            "11111111111",
            "123.456.789-00",
            "abc12345678"
    })
    @DisplayName("When converting DTO with invalid document Then should throw UserIllegalArgumentException")
    void whenConvertingDtoWithInvalidDocument_thenShouldThrowException(String invalidDocument) {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                invalidDocument,
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(dto);
        });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    @DisplayName("When converting DTO with null or empty document Then should throw UserIllegalArgumentException")
    void whenConvertingDtoWithNullOrEmptyDocument_thenShouldThrowException(String document) {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                document,
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(dto);
        });
    }

    @Test
    @DisplayName("When converting DTO with valid CPF with formatting Then should accept")
    void whenConvertingDtoWithValidCpfWithFormatting_thenShouldAccept() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "005.883.809-03",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        assertDoesNotThrow(() -> {
            CreateUserCommand command = mapper.toCommand(dto);
            assertNotNull(command);
        });
    }

    @Test
    @DisplayName("When converting DTO with birthdate today Then should map correctly")
    void whenConvertingDtoWithBirthdateToday_thenShouldMapCorrectly() {
        // Given
        LocalDate today = LocalDate.now();
        CreateUserDto dto = new CreateUserDto(
                "Recém Nascido",
                "00588380903",
                "bebe@test.com",
                "11999999999",
                today,
                Category.SON
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertEquals(today, command.getBirthdate());
    }

    @Test
    @DisplayName("When converting DTO with very old birthdate Then should map correctly")
    void whenConvertingDtoWithVeryOldBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        CreateUserDto dto = new CreateUserDto(
                "Pessoa Muito Idosa",
                "00588380903",
                "idoso@test.com",
                "11999999999",
                oldDate,
                Category.GRANDFATHER
        );

        // When
        CreateUserCommand command = mapper.toCommand(dto);

        // Then
        assertEquals(oldDate, command.getBirthdate());
        assertEquals(Category.GRANDFATHER, command.getCategory());
    }

    @Test
    @DisplayName("When mapping throws unexpected exception Then should throw UserIllegalArgumentException")
    void whenMappingThrowsUnexpectedException_thenShouldThrowUserIllegalArgumentException() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                null,
                Category.FATHER
        );

        // When & Then
        assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(dto);
        });
    }

    @Test
    @DisplayName("When exception occurs Then should contain proper error code")
    void whenExceptionOccurs_thenShouldContainProperErrorCode() {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "João Silva",
                "12345678900",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toCommand(dto);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }
}