package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
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

@DisplayName("Given UpdateUserCommandToUser")
class UpdateUserCommandToUserTest {

    private UpdateUserCommandToUser mapper;
    private UpdateUserCommand validCommand;

    @BeforeEach
    void setUp() {
        mapper = new UpdateUserCommandToUser();

        validCommand = new UpdateUserCommand();
        validCommand.setDocument("00588380903");
        validCommand.setFullName("João Silva");
        validCommand.setEmail("joao@test.com");
        validCommand.setPhone("11999999999");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);
        validCommand.setActive(true);
    }

    @Test
    @DisplayName("When mapping valid command Then should return User with all fields")
    void whenMappingValidCommand_thenShouldReturnUserWithAllFields() {
        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertNotNull(result);
        assertEquals("00588380903", result.getDocument());
        assertEquals("João Silva", result.getFullName());
        assertEquals("joao@test.com", result.getEmail());
        assertEquals("11999999999", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertEquals(Category.FATHER, result.getCategory());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("When mapping command with active true Then should return User with active true")
    void whenMappingCommandWithActiveTrue_thenShouldReturnUserWithActiveTrue() {
        // Given
        validCommand.setActive(true);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("When mapping command with active false Then should return User with active false")
    void whenMappingCommandWithActiveFalse_thenShouldReturnUserWithActiveFalse() {
        // Given
        validCommand.setActive(false);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertFalse(result.isActive());
    }

    @Test
    @DisplayName("When mapping command with null active Then should default to true")
    void whenMappingCommandWithNullActive_thenShouldDefaultToTrue() {
        // Given
        validCommand.setActive(null);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("When mapping command with null fullName Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullFullName_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setFullName(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
        assertEquals("Erro ao mapear UpdateUserCommand para User", exception.getMessage());
    }

    @Test
    @DisplayName("When mapping command with null document Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullDocument_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setDocument(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
        assertEquals("Erro ao mapear UpdateUserCommand para User", exception.getMessage());
    }

    @Test
    @DisplayName("When mapping command with null email Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullEmail_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setEmail(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
    }

    @Test
    @DisplayName("When mapping command with null phone Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullPhone_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setPhone(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
    }

    @Test
    @DisplayName("When mapping command with null birthdate Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullBirthdate_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setBirthdate(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
    }

    @Test
    @DisplayName("When mapping command with null category Then should throw UserIllegalArgumentException")
    void whenMappingCommandWithNullCategory_thenShouldThrowUserIllegalArgumentException() {
        // Given
        validCommand.setCategory(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When mapping command with each category Then should map correctly")
    void whenMappingCommandWithEachCategory_thenShouldMapCorrectly(Category category) {
        // Given
        validCommand.setCategory(category);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals(category, result.getCategory());
    }

    @Test
    @DisplayName("When mapping command with special characters in name Then should map correctly")
    void whenMappingCommandWithSpecialCharactersInName_thenShouldMapCorrectly() {
        // Given
        validCommand.setFullName("José María Ñoño de Souza");

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"joao@test.com", "maria@example.com", "user@domain.com.br"})
    @DisplayName("When mapping command with different emails Then should map correctly")
    void whenMappingCommandWithDifferentEmails_thenShouldMapCorrectly(String email) {
        // Given
        validCommand.setEmail(email);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals(email, result.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"11999999999", "11988888888", "21987654321"})
    @DisplayName("When mapping command with different phones Then should map correctly")
    void whenMappingCommandWithDifferentPhones_thenShouldMapCorrectly(String phone) {
        // Given
        validCommand.setPhone(phone);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals(phone, result.getPhone());
    }

    @Test
    @DisplayName("When mapping command with past birthdate Then should map correctly")
    void whenMappingCommandWithPastBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate pastDate = LocalDate.of(1950, 1, 1);
        validCommand.setBirthdate(pastDate);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals(pastDate, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping command with recent birthdate Then should map correctly")
    void whenMappingCommandWithRecentBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate recentDate = LocalDate.of(2020, 12, 31);
        validCommand.setBirthdate(recentDate);

        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertEquals(recentDate, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping multiple commands Then should process each independently")
    void whenMappingMultipleCommands_thenShouldProcessEachIndependently() {
        // Given
        UpdateUserCommand command1 = new UpdateUserCommand();
        command1.setDocument("00588380903");
        command1.setFullName("User 1");
        command1.setEmail("user1@test.com");
        command1.setPhone("11999999999");
        command1.setBirthdate(LocalDate.of(1990, 1, 1));
        command1.setCategory(Category.FATHER);
        command1.setActive(true);

        UpdateUserCommand command2 = new UpdateUserCommand();
        command2.setDocument("98765432100");
        command2.setFullName("User 2");
        command2.setEmail("user2@test.com");
        command2.setPhone("11988888888");
        command2.setBirthdate(LocalDate.of(1995, 5, 15));
        command2.setCategory(Category.MOTHER);
        command2.setActive(false);

        // When
        User result1 = mapper.toUser(command1);
        User result2 = mapper.toUser(command2);

        // Then
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertTrue(result1.isActive());
        assertFalse(result2.isActive());
    }

    @Test
    @DisplayName("When exception occurs during mapping Then should wrap in UserIllegalArgumentException")
    void whenExceptionOccursDuringMapping_thenShouldWrapInUserIllegalArgumentException() {
        // Given
        validCommand.setDocument(null);

        // When & Then
        UserIllegalArgumentException exception = assertThrows(UserIllegalArgumentException.class, () -> {
            mapper.toUser(validCommand);
        });

        assertEquals("USER-UPDATE-002", exception.getCode());
        assertEquals("Erro ao mapear UpdateUserCommand para User", exception.getMessage());
    }
}