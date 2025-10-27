package com.atuantes.mentes.user.domain.entity;

import com.atuantes.mentes.user.domain.exception.UserInvalidDocumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given User entity")
class UserTest {

    @Test
    @DisplayName("When creating user with valid data Then should create successfully")
    void whenCreatingUserWithValidData_thenShouldCreateSuccessfully() {
        // Given
        String fullName = "João Silva";
        String document = "00588380903";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When
        User user = new User(fullName, document, email, phone, birthdate, category);

        // Then
        assertNotNull(user);
        assertEquals(fullName, user.getFullName());
        assertEquals(document, user.getDocument());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(birthdate, user.getBirthdate());
        assertEquals(category, user.getCategory());
        assertTrue(user.isActive());
        assertNull(user.getId());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When creating user with each category Then should create successfully")
    void whenCreatingUserWithEachCategory_thenShouldCreateSuccessfully(Category category) {
        // Given
        String fullName = "Test User";
        String document = "00588380903";
        String email = "test@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);

        // When
        User user = new User(fullName, document, email, phone, birthdate, category);

        // Then
        assertEquals(category, user.getCategory());
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("When creating user with all parameters Then should create with provided values")
    void whenCreatingUserWithAllParameters_thenShouldCreateWithProvidedValues() {
        // Given
        UUID id = UUID.randomUUID();
        String fullName = "João Silva";
        boolean active = false;
        String document = "00588380903";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When
        // When
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setActive(active);
        user.setDocument(document);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBirthdate(birthdate);
        user.setCategory(category);

        // Then
        assertEquals(id, user.getId());
        assertEquals(fullName, user.getFullName());
        assertFalse(user.isActive());
        assertEquals(document, user.getDocument());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(birthdate, user.getBirthdate());
        assertEquals(category, user.getCategory());
    }

    @Test
    @DisplayName("When creating user with no-args constructor Then should create empty user")
    void whenCreatingUserWithNoArgsConstructor_thenShouldCreateEmptyUser() {
        // When
        User user = new User();

        // Then
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getFullName());
        assertNull(user.getDocument());
        assertNull(user.getEmail());
        assertNull(user.getPhone());
        assertNull(user.getBirthdate());
        assertNull(user.getCategory());
    }

    @Test
    @DisplayName("When creating user with null fullName Then should throw NullPointerException")
    void whenCreatingUserWithNullFullName_thenShouldThrowException() {
        // Given
        String document = "00588380903";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(null, document, email, phone, birthdate, category);
        });
    }

    @Test
    @DisplayName("When creating user with null document Then should throw NullPointerException")
    void whenCreatingUserWithNullDocument_thenShouldThrowException() {
        // Given
        String fullName = "João Silva";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(fullName, null, email, phone, birthdate, category);
        });
    }

    @Test
    @DisplayName("When creating user with null email Then should throw NullPointerException")
    void whenCreatingUserWithNullEmail_thenShouldThrowException() {
        // Given
        String fullName = "João Silva";
        String document = "00588380903";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(fullName, document, null, phone, birthdate, category);
        });
    }

    @Test
    @DisplayName("When creating user with null phone Then should throw NullPointerException")
    void whenCreatingUserWithNullPhone_thenShouldThrowException() {
        // Given
        String fullName = "João Silva";
        String document = "00588380903";
        String email = "joao@test.com";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(fullName, document, email, null, birthdate, category);
        });
    }

    @Test
    @DisplayName("When creating user with null birthdate Then should throw NullPointerException")
    void whenCreatingUserWithNullBirthdate_thenShouldThrowException() {
        // Given
        String fullName = "João Silva";
        String document = "00588380903";
        String email = "joao@test.com";
        String phone = "11999999999";
        Category category = Category.FATHER;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(fullName, document, email, phone, null, category);
        });
    }

    @Test
    @DisplayName("When creating user with null category Then should throw NullPointerException")
    void whenCreatingUserWithNullCategory_thenShouldThrowException() {
        // Given
        String fullName = "João Silva";
        String document = "00588380903";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            new User(fullName, document, email, phone, birthdate, null);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678900",
            "00000000000",
            "11111111111",
            "abc12345678"
    })
    @DisplayName("When creating user with invalid document Then should throw UserInvalidDocumentException")
    void whenCreatingUserWithInvalidDocument_thenShouldThrowException(String invalidDocument) {
        // Given
        String fullName = "João Silva";
        String email = "joao@test.com";
        String phone = "11999999999";
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        Category category = Category.FATHER;

        // When & Then
        assertThrows(UserInvalidDocumentException.class, () -> {
            new User(fullName, invalidDocument, email, phone, birthdate, category);
        });
    }

    @Test
    @DisplayName("When using setters Then should update values correctly")
    void whenUsingSetters_thenShouldUpdateValuesCorrectly() {
        // Given
        User user = new User();
        UUID newId = UUID.randomUUID();
        String newFullName = "Maria Santos";
        String newDocument = "00588380903";
        String newEmail = "maria@test.com";
        String newPhone = "11988888888";
        LocalDate newBirthdate = LocalDate.of(1995, 5, 15);
        Category newCategory = Category.MOTHER;
        LocalDateTime newCreatedAt = LocalDateTime.now();
        LocalDateTime newUpdatedAt = LocalDateTime.now();

        // When
        user.setId(newId);
        user.setFullName(newFullName);
        user.setActive(false);
        user.setDocument(newDocument);
        user.setEmail(newEmail);
        user.setPhone(newPhone);
        user.setBirthdate(newBirthdate);
        user.setCategory(newCategory);
        user.setCreatedAt(newCreatedAt);
        user.setUpdatedAt(newUpdatedAt);

        // Then
        assertEquals(newId, user.getId());
        assertEquals(newFullName, user.getFullName());
        assertFalse(user.isActive());
        assertEquals(newDocument, user.getDocument());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newPhone, user.getPhone());
        assertEquals(newBirthdate, user.getBirthdate());
        assertEquals(newCategory, user.getCategory());
        assertEquals(newCreatedAt, user.getCreatedAt());
        assertEquals(newUpdatedAt, user.getUpdatedAt());
    }

    @Test
    @DisplayName("When setting active to true Then should be active")
    void whenSettingActiveToTrue_thenShouldBeActive() {
        // Given
        User user = new User();
        user.setActive(false);

        // When
        user.setActive(true);

        // Then
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("When created with valid constructor Then active should be true by default")
    void whenCreatedWithValidConstructor_thenActiveShouldBeTrueByDefault() {
        // Given & When
        User user = new User(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // Then
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("When setting timestamps Then should store correctly")
    void whenSettingTimestamps_thenShouldStoreCorrectly() {
        // Given
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        // When
        user.setCreatedAt(now);
        user.setUpdatedAt(now.plusMinutes(5));

        // Then
        assertEquals(now, user.getCreatedAt());
        assertEquals(now.plusMinutes(5), user.getUpdatedAt());
    }

    @Test
    @DisplayName("When creating user with special characters in name Then should accept")
    void whenCreatingUserWithSpecialCharactersInName_thenShouldAccept() {
        // Given
        String fullName = "José María Ñoño de Souza";

        // When
        User user = new User(
                fullName,
                "00588380903",
                "jose@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // Then
        assertEquals(fullName, user.getFullName());
    }

    @Test
    @DisplayName("When creating user with formatted CPF Then should accept")
    void whenCreatingUserWithFormattedCpf_thenShouldAccept() {
        // Given
        String document = "005.883.809-03";

        // When & Then
        assertDoesNotThrow(() -> {
            User user = new User(
                    "João Silva",
                    document,
                    "joao@test.com",
                    "11999999999",
                    LocalDate.of(1990, 1, 1),
                    Category.FATHER
            );
            assertEquals(document, user.getDocument());
        });
    }
}