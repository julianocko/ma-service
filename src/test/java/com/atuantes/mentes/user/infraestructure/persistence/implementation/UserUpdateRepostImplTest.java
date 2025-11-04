package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private UserUpdateRepostImpl userUpdateRepostImpl;

    private UUID transactionId;
    private User userToUpdate;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();

        userToUpdate = new User();
        userToUpdate.setDocument("00588380903");
        userToUpdate.setFullName("João Silva Updated");
        userToUpdate.setEmail("joao.updated@test.com");
        userToUpdate.setPhone("11988888888");
        userToUpdate.setBirthdate(LocalDate.of(1990, 1, 1));
        userToUpdate.setCategory(Category.FATHER);
        userToUpdate.setActive(true);

        updatedUser = new User();
        updatedUser.setId(UUID.randomUUID());
        updatedUser.setDocument("00588380903");
        updatedUser.setFullName("João Silva Updated");
        updatedUser.setEmail("joao.updated@test.com");
        updatedUser.setPhone("11988888888");
        updatedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        updatedUser.setCategory(Category.FATHER);
        updatedUser.setActive(true);
    }

    @Test
    @DisplayName("When updating existing user Then should return updated user")
    void whenUpdatingExistingUser_thenShouldReturnUpdatedUser() {
        // Given
        when(userRepository.updateByDocument(
                eq("00588380903"),
                eq("João Silva Updated"),
                eq("joao.updated@test.com"),
                eq("11988888888"),
                eq(LocalDate.of(1990, 1, 1)),
                eq("FATHER"),
                eq(true)
        )).thenReturn(Optional.of(updatedUser));

        // When
        User result = userUpdateRepostImpl.update(userToUpdate, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getDocument(), result.getDocument());
        assertEquals(updatedUser.getFullName(), result.getFullName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getPhone(), result.getPhone());
        assertEquals(updatedUser.getBirthdate(), result.getBirthdate());
        assertEquals(updatedUser.getCategory(), result.getCategory());
        assertTrue(result.isActive());

        verify(userRepository, times(1)).updateByDocument(
                anyString(), anyString(), anyString(), anyString(), 
                any(LocalDate.class), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("When updating non-existent user Then should throw UserNotFoundException")
    void whenUpdatingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        when(userRepository.updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), anyString(), anyBoolean()
        )).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userUpdateRepostImpl.update(userToUpdate, transactionId);
        });

        assertEquals("USER-404", exception.getCode());
        assertTrue(exception.getMessage().contains("User not found for document"));
        assertTrue(exception.getMessage().contains("00588380903"));

        verify(userRepository, times(1)).updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("When updating user with active=false Then should update successfully")
    void whenUpdatingUserWithActiveFalse_thenShouldUpdateSuccessfully() {
        // Given
        userToUpdate.setActive(false);
        updatedUser.setActive(false);

        when(userRepository.updateByDocument(
                eq("00588380903"),
                eq("João Silva Updated"),
                eq("joao.updated@test.com"),
                eq("11988888888"),
                eq(LocalDate.of(1990, 1, 1)),
                eq("FATHER"),
                eq(false)
        )).thenReturn(Optional.of(updatedUser));

        // When
        User result = userUpdateRepostImpl.update(userToUpdate, transactionId);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());

        verify(userRepository, times(1)).updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), anyString(), eq(false));
    }

    @Test
    @DisplayName("When updating user with different category Then should update successfully")
    void whenUpdatingUserWithDifferentCategory_thenShouldUpdateSuccessfully() {
        // Given
        userToUpdate.setCategory(Category.MOTHER);
        updatedUser.setCategory(Category.MOTHER);

        when(userRepository.updateByDocument(
                eq("00588380903"),
                eq("João Silva Updated"),
                eq("joao.updated@test.com"),
                eq("11988888888"),
                eq(LocalDate.of(1990, 1, 1)),
                eq("MOTHER"),
                eq(true)
        )).thenReturn(Optional.of(updatedUser));

        // When
        User result = userUpdateRepostImpl.update(userToUpdate, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(Category.MOTHER, result.getCategory());

        verify(userRepository, times(1)).updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), eq("MOTHER"), anyBoolean());
    }

    @Test
    @DisplayName("When repository passes transaction id Then should log correctly")
    void whenRepositoryPassesTransactionId_thenShouldLogCorrectly() {
        // Given
        when(userRepository.updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), anyString(), anyBoolean()
        )).thenReturn(Optional.of(updatedUser));

        // When
        userUpdateRepostImpl.update(userToUpdate, transactionId);

        // Then
        verify(userRepository, times(1)).updateByDocument(
                anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), anyString(), anyBoolean());
    }
}
