package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserInternalServerException;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given UserUpdateRepostImpl")
class UserUpdateRepostImplTest {

    @Mock
    private UserRepository userRepository;

    private UserUpdateRepostImpl userUpdate;
    private UUID transactionId;
    private User user;

    @BeforeEach
    void setUp() {
        userUpdate = new UserUpdateRepostImpl(userRepository);
        transactionId = UUID.randomUUID();

        user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("João Silva");
        user.setDocument("00588380903");
        user.setEmail("joao@test.com");
        user.setPhone("11999999999");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCategory(Category.FATHER);
        user.setActive(true);
    }

    @Test
    @DisplayName("When updating existing user Then should return updated user")
    void whenUpdatingExistingUser_thenShouldReturnUpdatedUser() {
        // Given
        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setFullName("João Silva Atualizado");
        updatedUser.setDocument(user.getDocument());
        updatedUser.setEmail("joao.novo@test.com");
        updatedUser.setPhone("11988888888");
        updatedUser.setBirthdate(user.getBirthdate());
        updatedUser.setCategory(Category.FATHER);
        updatedUser.setActive(true);

        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        )).thenReturn(Optional.of(updatedUser));

        // When
        User result = userUpdate.update(user, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getFullName(), result.getFullName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getPhone(), result.getPhone());

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When updating non-existent user Then should throw UserNotFoundException")
    void whenUpdatingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        )).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userUpdate.update(user, transactionId);
        });

        assertEquals("USER-404", exception.getCode());
        assertTrue(exception.getMessage().contains("User not found for document"));
        assertTrue(exception.getMessage().contains(user.getDocument()));

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When repository throws exception Then should throw UserInternalServerException")
    void whenRepositoryThrowsException_thenShouldThrowUserInternalServerException() {
        // Given
        when(userRepository.updateByDocument(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                anyBoolean()
        )).thenThrow(new RuntimeException("Database error"));

        // When & Then
        UserInternalServerException exception = assertThrows(UserInternalServerException.class, () -> {
            userUpdate.update(user, transactionId);
        });

        assertEquals("USER-UPDATE-ERROR", exception.getCode());
        assertEquals("Erro ao atualizar usuário", exception.getMessage());

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When updating user to inactive Then should update correctly")
    void whenUpdatingUserToInactive_thenShouldUpdateCorrectly() {
        // Given
        user.setActive(false);
        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(false)
        )).thenReturn(Optional.of(user));

        // When
        User result = userUpdate.update(user, transactionId);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                false
        );
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When updating user with each category Then should update correctly")
    void whenUpdatingUserWithEachCategory_thenShouldUpdateCorrectly(Category category) {
        // Given
        user.setCategory(category);
        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(category.name()),
                eq(user.isActive())
        )).thenReturn(Optional.of(user));

        // When
        User result = userUpdate.update(user, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                category.name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When updating user with special characters in name Then should update correctly")
    void whenUpdatingUserWithSpecialCharactersInName_thenShouldUpdateCorrectly() {
        // Given
        user.setFullName("José María Ñoño de Souza");
        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq("José María Ñoño de Souza"),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        )).thenReturn(Optional.of(user));

        // When
        User result = userUpdate.update(user, transactionId);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @Test
    @DisplayName("When updating multiple users Then should process each independently")
    void whenUpdatingMultipleUsers_thenShouldProcessEachIndependently() {
        // Given
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setFullName("User 1");
        user1.setDocument("00588380903");
        user1.setEmail("user1@test.com");
        user1.setPhone("11999999999");
        user1.setBirthdate(LocalDate.of(1990, 1, 1));
        user1.setCategory(Category.FATHER);
        user1.setActive(true);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setFullName("User 2");
        user2.setDocument("98765432100");
        user2.setEmail("user2@test.com");
        user2.setPhone("11988888888");
        user2.setBirthdate(LocalDate.of(1995, 5, 15));
        user2.setCategory(Category.MOTHER);
        user2.setActive(false);

        when(userRepository.updateByDocument(
                eq(user1.getDocument()),
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                anyBoolean()
        )).thenReturn(Optional.of(user1));

        when(userRepository.updateByDocument(
                eq(user2.getDocument()),
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                anyBoolean()
        )).thenReturn(Optional.of(user2));

        // When
        User result1 = userUpdate.update(user1, transactionId);
        User result2 = userUpdate.update(user2, transactionId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());

        verify(userRepository, times(1)).updateByDocument(
                user1.getDocument(),
                user1.getFullName(),
                user1.getEmail(),
                user1.getPhone(),
                user1.getBirthdate(),
                user1.getCategory().name(),
                user1.isActive()
        );

        verify(userRepository, times(1)).updateByDocument(
                user2.getDocument(),
                user2.getFullName(),
                user2.getEmail(),
                user2.getPhone(),
                user2.getBirthdate(),
                user2.getCategory().name(),
                user2.isActive()
        );
    }

    @Test
    @DisplayName("When updating user with different transaction ids Then should process each independently")
    void whenUpdatingUserWithDifferentTransactionIds_thenShouldProcessEachIndependently() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();

        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        )).thenReturn(Optional.of(user));

        // When
        User result1 = userUpdate.update(user, transactionId1);
        User result2 = userUpdate.update(user, transactionId2);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);

        verify(userRepository, times(2)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When UserNotFoundException is thrown Then should propagate exception")
    void whenUserNotFoundExceptionIsThrown_thenShouldPropagateException() {
        // Given
        when(userRepository.updateByDocument(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                anyBoolean()
        )).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userUpdate.update(user, transactionId);
        });

        verify(userRepository, times(1)).updateByDocument(
                user.getDocument(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory().name(),
                user.isActive()
        );
    }

    @Test
    @DisplayName("When updating user with all fields Then should pass all parameters correctly")
    void whenUpdatingUserWithAllFields_thenShouldPassAllParametersCorrectly() {
        // Given
        when(userRepository.updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        )).thenReturn(Optional.of(user));

        // When
        userUpdate.update(user, transactionId);

        // Then
        verify(userRepository, times(1)).updateByDocument(
                eq(user.getDocument()),
                eq(user.getFullName()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getBirthdate()),
                eq(user.getCategory().name()),
                eq(user.isActive())
        );
    }

    @Test
    @DisplayName("When repository returns empty optional Then should include document in error message")
    void whenRepositoryReturnsEmptyOptional_thenShouldIncludeDocumentInErrorMessage() {
        // Given
        when(userRepository.updateByDocument(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                anyString(),
                anyBoolean()
        )).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userUpdate.update(user, transactionId);
        });

        assertTrue(exception.getMessage().contains(user.getDocument()));
    }
}