package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given CreateUserCommandToUser mapper")
class CreateUserCommandToUserTest {

    private CreateUserCommandToUser mapper;
    private CreateUserCommand validCommand;

    @BeforeEach
    void setUp() {
        mapper = new CreateUserCommandToUser();

        validCommand = new CreateUserCommand();
        validCommand.setFullName("João Silva");
        validCommand.setDocument("00588380903");
        validCommand.setEmail("joao@test.com");
        validCommand.setPhone("11999999999");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When converting valid command Then should map all fields correctly")
    void whenConvertingValidCommand_thenShouldMapAllFieldsCorrectly() {
        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertNotNull(user);
        assertEquals("João Silva", user.getFullName());
        assertEquals("00588380903", user.getDocument());
        assertEquals("joao@test.com", user.getEmail());
        assertEquals("11999999999", user.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthdate());
        assertEquals(Category.FATHER, user.getCategory());
        assertTrue(user.isActive());
        assertNull(user.getId());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When converting command with each category Then should map correctly")
    void whenConvertingCommandWithEachCategory_thenShouldMapCorrectly(Category category) {
        // Given
        validCommand.setCategory(category);

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertNotNull(user);
        assertEquals(category, user.getCategory());
        assertEquals("João Silva", user.getFullName());
    }

    @Test
    @DisplayName("When converting command with special characters in name Then should preserve them")
    void whenConvertingCommandWithSpecialCharactersInName_thenShouldPreserveThem() {
        // Given
        validCommand.setFullName("José María Ñoño de Souza");

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertEquals("José María Ñoño de Souza", user.getFullName());
    }

    @Test
    @DisplayName("When converting command with phone with country code Then should preserve format")
    void whenConvertingCommandWithPhoneWithCountryCode_thenShouldPreserveFormat() {
        // Given
        validCommand.setPhone("+5511999999999");

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertEquals("+5511999999999", user.getPhone());
    }

    @Test
    @DisplayName("When converting command with email in uppercase Then should preserve case")
    void whenConvertingCommandWithEmailInUppercase_thenShouldPreserveCase() {
        // Given
        validCommand.setEmail("JOAO@TEST.COM");

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertEquals("JOAO@TEST.COM", user.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678900",
            "00000000000",
            "11111111111",
            "abc12345678"
    })
    @DisplayName("When converting command with invalid document Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithInvalidDocument_thenShouldThrowException(String invalidDocument) {
        // Given
        validCommand.setDocument(invalidDocument);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with valid CPF with formatting Then should accept")
    void whenConvertingCommandWithValidCpfWithFormatting_thenShouldAccept() {
        // Given
        validCommand.setDocument("005.883.809-03");

        // When & Then
        assertDoesNotThrow(() -> {
            User user = mapper.toUser(validCommand);
            assertNotNull(user);
            assertEquals("005.883.809-03", user.getDocument());
        });
    }

    @Test
    @DisplayName("When converting command with null fullName Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullFullName_thenShouldThrowException() {
        // Given
        validCommand.setFullName(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with null document Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullDocument_thenShouldThrowException() {
        // Given
        validCommand.setDocument(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with null email Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullEmail_thenShouldThrowException() {
        // Given
        validCommand.setEmail(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with null phone Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullPhone_thenShouldThrowException() {
        // Given
        validCommand.setPhone(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with null birthdate Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullBirthdate_thenShouldThrowException() {
        // Given
        validCommand.setBirthdate(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with null category Then should throw UserIllegalArgumentException")
    void whenConvertingCommandWithNullCategory_thenShouldThrowException() {
        // Given
        validCommand.setCategory(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("When converting command with birthdate today Then should map correctly")
    void whenConvertingCommandWithBirthdateToday_thenShouldMapCorrectly() {
        // Given
        LocalDate today = LocalDate.now();
        validCommand.setBirthdate(today);

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertEquals(today, user.getBirthdate());
    }

    @Test
    @DisplayName("When converting command with very old birthdate Then should map correctly")
    void whenConvertingCommandWithVeryOldBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate oldDate = LocalDate.of(1900, 1, 1);
        validCommand.setBirthdate(oldDate);
        validCommand.setCategory(Category.GRANDFATHER);

        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertEquals(oldDate, user.getBirthdate());
        assertEquals(Category.GRANDFATHER, user.getCategory());
    }

    @Test
    @DisplayName("When exception occurs Then should contain proper error code and message")
    void whenExceptionOccurs_thenShouldContainProperErrorCodeAndMessage() {
        // Given
        validCommand.setDocument("12345678900");

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
        assertFalse(exception.getMessage().isEmpty());
    }

    @Test
    @DisplayName("When created user Then should be active by default")
    void whenCreatedUser_thenShouldBeActiveByDefault() {
        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("When created user Then should have null id")
    void whenCreatedUser_thenShouldHaveNullId() {
        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertNull(user.getId());
    }

    @Test
    @DisplayName("When created user Then should have null timestamps")
    void whenCreatedUser_thenShouldHaveNullTimestamps() {
        // When
        User user = mapper.toUser(validCommand);

        // Then
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    @DisplayName("When converting multiple commands Then should create independent users")
    void whenConvertingMultipleCommands_thenShouldCreateIndependentUsers() {
        // Given
        CreateUserCommand command1 = new CreateUserCommand();
        command1.setFullName("User 1");
        command1.setDocument("00588380903");
        command1.setEmail("user1@test.com");
        command1.setPhone("11999999999");
        command1.setBirthdate(LocalDate.of(1990, 1, 1));
        command1.setCategory(Category.FATHER);

        CreateUserCommand command2 = new CreateUserCommand();
        command2.setFullName("User 2");
        command2.setDocument("00588380903");
        command2.setEmail("user2@test.com");
        command2.setPhone("11988888888");
        command2.setBirthdate(LocalDate.of(1995, 5, 15));
        command2.setCategory(Category.MOTHER);

        // When
        User user1 = mapper.toUser(command1);
        User user2 = mapper.toUser(command2);

        // Then
        assertNotSame(user1, user2);
        assertEquals("User 1", user1.getFullName());
        assertEquals("User 2", user2.getFullName());
        assertEquals(Category.FATHER, user1.getCategory());
        assertEquals(Category.MOTHER, user2.getCategory());
    }
}