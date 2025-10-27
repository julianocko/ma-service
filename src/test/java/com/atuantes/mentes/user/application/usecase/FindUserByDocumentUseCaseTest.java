package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given FindUserByDocumentUseCase")
class FindUserByDocumentUseCaseTest {

    @Mock
    private FindUserByDocument findUserByDocument;

    private FindUserByDocumentUseCase useCase;
    private UUID transactionId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByDocumentUseCase(findUserByDocument);
        transactionId = UUID.randomUUID();

        expectedUser = new User();
        expectedUser.setId(UUID.randomUUID());
        expectedUser.setFullName("João Silva");
        expectedUser.setActive(true);
        expectedUser.setDocument("00588380903");
        expectedUser.setEmail("joao@test.com");
        expectedUser.setPhone("11999999999");
        expectedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        expectedUser.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When finding user by valid document Then should return user")
    void whenFindingUserByValidDocument_thenShouldReturnUser() {
        // Given
        String document = "00588380903";
        when(findUserByDocument.execute(document, transactionId)).thenReturn(expectedUser);

        // When
        User result = useCase.findUserByDocument(document, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getFullName(), result.getFullName());
        assertEquals(expectedUser.getDocument(), result.getDocument());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getPhone(), result.getPhone());
        assertEquals(expectedUser.getBirthdate(), result.getBirthdate());
        assertEquals(expectedUser.getCategory(), result.getCategory());
        assertTrue(result.isActive());

        verify(findUserByDocument, times(1)).execute(document, transactionId);
    }

    @Test
    @DisplayName("When finding non-existent user Then should throw UserNotFoundException")
    void whenFindingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        when(findUserByDocument.execute(document, transactionId))
                .thenThrow(new UserNotFoundException("USER-404", "User not found"));

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            useCase.findUserByDocument(document, transactionId);
        });

        assertEquals("USER-404", exception.getCode());
        assertEquals("User not found", exception.getMessage());

        verify(findUserByDocument, times(1)).execute(document, transactionId);
    }

    @Test
    @DisplayName("When service throws exception Then should propagate exception")
    void whenServiceThrowsException_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        when(findUserByDocument.execute(document, transactionId))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.findUserByDocument(document, transactionId);
        });

        assertEquals("Database error", exception.getMessage());

        verify(findUserByDocument, times(1)).execute(document, transactionId);
    }

    @Test
    @DisplayName("When finding user with different transaction ids Then should use correct transaction id")
    void whenFindingUserWithDifferentTransactionIds_thenShouldUseCorrectTransactionId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        when(findUserByDocument.execute(eq(document), any(UUID.class))).thenReturn(expectedUser);

        // When
        useCase.findUserByDocument(document, transactionId1);
        useCase.findUserByDocument(document, transactionId2);

        // Then
        verify(findUserByDocument, times(1)).execute(document, transactionId1);
        verify(findUserByDocument, times(1)).execute(document, transactionId2);
    }

    @Test
    @DisplayName("When finding multiple users by different documents Then should process each independently")
    void whenFindingMultipleUsersByDifferentDocuments_thenShouldProcessEachIndependently() {
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

        when(findUserByDocument.execute(document1, transactionId)).thenReturn(user1);
        when(findUserByDocument.execute(document2, transactionId)).thenReturn(user2);

        // When
        User result1 = useCase.findUserByDocument(document1, transactionId);
        User result2 = useCase.findUserByDocument(document2, transactionId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals(document1, result1.getDocument());
        assertEquals(document2, result2.getDocument());

        verify(findUserByDocument, times(1)).execute(document1, transactionId);
        verify(findUserByDocument, times(1)).execute(document2, transactionId);
    }

    @Test
    @DisplayName("When finding user Then should pass correct parameters to service")
    void whenFindingUser_thenShouldPassCorrectParametersToService() {
        // Given
        String document = "00588380903";
        when(findUserByDocument.execute(document, transactionId)).thenReturn(expectedUser);

        // When
        useCase.findUserByDocument(document, transactionId);

        // Then
        verify(findUserByDocument, times(1)).execute(eq(document), eq(transactionId));
    }

    @Test
    @DisplayName("When use case is created Then should have non-null dependency")
    void whenUseCaseIsCreated_thenShouldHaveNonNullDependency() {
        // When & Then
        assertNotNull(useCase.findUserByDocument());
    }

    @Test
    @DisplayName("When finding inactive user Then should return inactive user")
    void whenFindingInactiveUser_thenShouldReturnInactiveUser() {
        // Given
        String document = "00588380903";
        expectedUser.setActive(false);
        when(findUserByDocument.execute(document, transactionId)).thenReturn(expectedUser);

        // When
        User result = useCase.findUserByDocument(document, transactionId);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @Test
    @DisplayName("When finding user with each category Then should return user with correct category")
    void whenFindingUserWithEachCategory_thenShouldReturnUserWithCorrectCategory() {
        // Given
        String document = "00588380903";

        for (Category category : Category.values()) {
            expectedUser.setCategory(category);
            when(findUserByDocument.execute(document, transactionId)).thenReturn(expectedUser);

            // When
            User result = useCase.findUserByDocument(document, transactionId);

            // Then
            assertNotNull(result);
            assertEquals(category, result.getCategory());
        }

        verify(findUserByDocument, times(Category.values().length))
                .execute(document, transactionId);
    }

    @Test
    @DisplayName("When finding user with special characters in name Then should return user correctly")
    void whenFindingUserWithSpecialCharactersInName_thenShouldReturnUserCorrectly() {
        // Given
        String document = "00588380903";
        expectedUser.setFullName("José María Ñoño de Souza");
        when(findUserByDocument.execute(document, transactionId)).thenReturn(expectedUser);

        // When
        User result = useCase.findUserByDocument(document, transactionId);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }
}