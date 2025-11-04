package com.atuantes.mentes.user.domain.mapper;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
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
    @DisplayName("When mapping valid command Then should return User")
    void whenMappingValidCommand_thenShouldReturnUser() {
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
    @DisplayName("When mapping command with null active Then should default to true")
    void whenMappingCommandWithNullActive_thenShouldDefaultToTrue() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(null);

        // When
        User result = mapper.toUser(command);

        // Then
        assertNotNull(result);
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("When mapping command with active false Then should set active to false")
    void whenMappingCommandWithActiveFalse_thenShouldSetActiveToFalse() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(false);

        // When
        User result = mapper.toUser(command);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When mapping command with each category Then should map correctly")
    void whenMappingCommandWithEachCategory_thenShouldMapCorrectly(Category category) {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(category);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
    }

    @Test
    @DisplayName("When mapping command with special characters in name Then should map correctly")
    void whenMappingCommandWithSpecialCharactersInName_thenShouldMapCorrectly() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("José María Ñoño de Souza");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @Test
    @DisplayName("When mapping command with birthdate today Then should map correctly")
    void whenMappingCommandWithBirthdateToday_thenShouldMapCorrectly() {
        // Given
        LocalDate today = LocalDate.now();
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(today);
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals(today, result.getBirthdate());
    }

    @Test
    @DisplayName("When mapping command with past birthdate Then should map correctly")
    void whenMappingCommandWithPastBirthdate_thenShouldMapCorrectly() {
        // Given
        LocalDate pastDate = LocalDate.of(1950, 1, 1);
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(pastDate);
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals(pastDate, result.getBirthdate());
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
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals("00588380903", result1.getDocument());
        assertEquals("98765432100", result2.getDocument());
        assertTrue(result1.isActive());
        assertFalse(result2.isActive());
    }

    @Test
    @DisplayName("When mapping command with all fields filled Then should map all fields correctly")
    void whenMappingCommandWithAllFieldsFilled_thenShouldMapAllFieldsCorrectly() {
        // When
        User result = mapper.toUser(validCommand);

        // Then
        assertNotNull(result.getDocument());
        assertNotNull(result.getFullName());
        assertNotNull(result.getEmail());
        assertNotNull(result.getPhone());
        assertNotNull(result.getBirthdate());
        assertNotNull(result.getCategory());
    }

    @ParameterizedTest
    @ValueSource(strings = {"11999999999", "11988888888", "21987654321"})
    @DisplayName("When mapping command with different phones Then should map correctly")
    void whenMappingCommandWithDifferentPhones_thenShouldMapCorrectly(String phone) {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone(phone);
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals(phone, result.getPhone());
    }

    @ParameterizedTest
    @ValueSource(strings = {"joao@test.com", "maria@example.com", "user@domain.com.br"})
    @DisplayName("When mapping command with different emails Then should map correctly")
    void whenMappingCommandWithDifferentEmails_thenShouldMapCorrectly(String email) {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument("00588380903");
        command.setFullName("João Silva");
        command.setEmail(email);
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals(email, result.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00588380903", "12345678900", "98765432100"})
    @DisplayName("When mapping command with different documents Then should map correctly")
    void whenMappingCommandWithDifferentDocuments_thenShouldMapCorrectly(String document) {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument(document);
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertEquals(document, result.getDocument());
    }

    @Test
    @DisplayName("When mapping command with null document Then should map correctly")
    void whenMappingCommandWithNullDocument_thenShouldMapCorrectly() {
        // Given
        UpdateUserCommand command = new UpdateUserCommand();
        command.setDocument(null);
        command.setFullName("João Silva");
        command.setEmail("joao@test.com");
        command.setPhone("11999999999");
        command.setBirthdate(LocalDate.of(1990, 1, 1));
        command.setCategory(Category.FATHER);
        command.setActive(true);

        // When
        User result = mapper.toUser(command);

        // Then
        assertNotNull(result);
        assertNull(result.getDocument());
    }
}