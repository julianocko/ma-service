package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given UserDeleteRepostImpl")
class UserDeleteRepostImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDeleteRepostImpl userDelete;
    private UUID transactionId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userDelete = new UserDeleteRepostImpl(userRepository);
        transactionId = UUID.randomUUID();

        existingUser = new User();
        existingUser.setId(UUID.randomUUID());
        existingUser.setFullName("João Silva");
        existingUser.setActive(true);
        existingUser.setDocument("00588380903");
        existingUser.setEmail("joao@test.com");
        existingUser.setPhone("11999999999");
        existingUser.setBirthdate(LocalDate.of(1990, 1, 1));
        existingUser.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When deleting existing user Then should call repository methods")
    void whenDeletingExistingUser_thenShouldCallRepositoryMethods() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document, transactionId);

        // Then
        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, times(1)).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleting non-existent user Then should throw UserNotFoundException")
    void whenDeletingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDelete.deleteByDocument(document, transactionId);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
        assertEquals("USER-0007", exception.getCode());

        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, never()).deleteByDocument(document);
    }

    @Test
    @DisplayName("When repository throws exception during find Then should propagate exception")
    void whenRepositoryThrowsExceptionDuringFind_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDelete.deleteByDocument(document, transactionId);
        });

        assertEquals("Database error", exception.getMessage());

        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, never()).deleteByDocument(document);
    }

    @Test
    @DisplayName("When repository throws exception during delete Then should propagate exception")
    void whenRepositoryThrowsExceptionDuringDelete_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document))
                .thenThrow(new RuntimeException("Delete failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userDelete.deleteByDocument(document, transactionId);
        });

        assertEquals("Delete failed", exception.getMessage());

        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, times(1)).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleting with different transaction ids Then should delete independently")
    void whenDeletingWithDifferentTransactionIds_thenShouldDeleteIndependently() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document, transactionId1);
        userDelete.deleteByDocument(document, transactionId2);

        // Then
        verify(userRepository, times(2)).findByDocument(document);
        verify(userRepository, times(2)).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleting multiple users by different documents Then should process each independently")
    void whenDeletingMultipleUsersByDifferentDocuments_thenShouldProcessEachIndependently() {
        // Given
        String document1 = "00588380903";
        String document2 = "98765432100";

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setFullName("User 1");
        user1.setActive(true);
        user1.setDocument(document1);
        user1.setEmail("user1@test.com");
        user1.setPhone("11999999999");
        user1.setBirthdate(LocalDate.of(1990, 1, 1));
        user1.setCategory(Category.FATHER);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setFullName("User 2");
        user2.setActive(true);
        user2.setDocument(document2);
        user2.setEmail("user2@test.com");
        user2.setPhone("11988888888");
        user2.setBirthdate(LocalDate.of(1995, 5, 15));
        user2.setCategory(Category.MOTHER);

        when(userRepository.findByDocument(document1)).thenReturn(Optional.of(user1));
        when(userRepository.findByDocument(document2)).thenReturn(Optional.of(user2));
        when(userRepository.deleteByDocument(document1)).thenReturn(1);
        when(userRepository.deleteByDocument(document2)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document1, transactionId);
        userDelete.deleteByDocument(document2, transactionId);

        // Then
        verify(userRepository, times(1)).findByDocument(document1);
        verify(userRepository, times(1)).findByDocument(document2);
        verify(userRepository, times(1)).deleteByDocument(document1);
        verify(userRepository, times(1)).deleteByDocument(document2);
    }

    @Test
    @DisplayName("When deleting inactive user Then should delete successfully")
    void whenDeletingInactiveUser_thenShouldDeleteSuccessfully() {
        // Given
        String document = "00588380903";
        existingUser.setActive(false);
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document, transactionId);

        // Then
        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, times(1)).deleteByDocument(document);
    }

    @Test
    @DisplayName("When repository returns empty optional Then should throw UserNotFoundException with correct code")
    void whenRepositoryReturnsEmptyOptional_thenShouldThrowUserNotFoundExceptionWithCorrectCode() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userDelete.deleteByDocument(document, transactionId);
        });

        assertNotNull(exception.getCode());
        assertFalse(exception.getCode().isEmpty());
        assertEquals("USER-0007", exception.getCode());

        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, never()).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleting user Then should call repository with correct document")
    void whenDeletingUser_thenShouldCallRepositoryWithCorrectDocument() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document, transactionId);

        // Then
        verify(userRepository, times(1)).findByDocument(eq(document));
        verify(userRepository, times(1)).deleteByDocument(eq(document));
    }

    @Test
    @DisplayName("When deleting user multiple times Then should call repository each time")
    void whenDeletingUserMultipleTimes_thenShouldCallRepositoryEachTime() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
        when(userRepository.deleteByDocument(document)).thenReturn(1);

        // When
        userDelete.deleteByDocument(document, transactionId);
        userDelete.deleteByDocument(document, transactionId);
        userDelete.deleteByDocument(document, transactionId);

        // Then
        verify(userRepository, times(3)).findByDocument(document);
        verify(userRepository, times(3)).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleteByDocument returns zero rows Then should have already thrown UserNotFoundException")
    void whenDeleteByDocumentReturnsZeroRows_thenShouldHaveAlreadyThrownUserNotFoundException() {
        // Given - This scenario is prevented by the existence check
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userDelete.deleteByDocument(document, transactionId);
        });

        verify(userRepository, times(1)).findByDocument(document);
        verify(userRepository, never()).deleteByDocument(document);
    }

    @Test
    @DisplayName("When deleting user with each category Then should delete successfully")
    void whenDeletingUserWithEachCategory_thenShouldDeleteSuccessfully() {
        // Given
        String document = "00588380903";

        for (Category category : Category.values()) {
            existingUser.setCategory(category);
            when(userRepository.findByDocument(document)).thenReturn(Optional.of(existingUser));
            when(userRepository.deleteByDocument(document)).thenReturn(1);

            // When
            userDelete.deleteByDocument(document, transactionId);

            // Then - verify called
            verify(userRepository, atLeastOnce()).findByDocument(document);
            verify(userRepository, atLeastOnce()).deleteByDocument(document);
        }
    }
}
