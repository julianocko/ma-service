package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given FindUserByDocumentController")
class FindUserByDocumentControllerTest {

    @Mock
    private FindUserByDocumentUseCase findUserByDocumentUseCase;

    private FindUserByDocumentController controller;
    private UUID transactionId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        controller = new FindUserByDocumentController(findUserByDocumentUseCase);
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
    @DisplayName("When finding user by valid document Then should return user with status 200")
    void whenFindingUserByValidDocument_thenShouldReturnUserWithStatus200() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<User> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUser.getId(), response.getBody().getId());
        assertEquals(expectedUser.getFullName(), response.getBody().getFullName());
        assertEquals(expectedUser.getDocument(), response.getBody().getDocument());
        assertEquals(expectedUser.getEmail(), response.getBody().getEmail());
        assertEquals(expectedUser.getPhone(), response.getBody().getPhone());
        assertEquals(expectedUser.getBirthdate(), response.getBody().getBirthdate());
        assertEquals(expectedUser.getCategory(), response.getBody().getCategory());
        assertTrue(response.getBody().isActive());

        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When finding user by document with special characters Then should normalize and return user")
    void whenFindingUserByDocumentWithSpecialCharacters_thenShouldNormalizeAndReturnUser() {
        // Given
        String documentWithMask = "005.883.809-03";
        String normalizedDocument = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(normalizedDocument, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<User> response = controller.findByDocument(transactionId, documentWithMask);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUser.getDocument(), response.getBody().getDocument());

        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(normalizedDocument, transactionId);
    }

    @Test
    @DisplayName("When finding user by document with dots and dashes Then should remove all non-digits")
    void whenFindingUserByDocumentWithDotsAndDashes_thenShouldRemoveAllNonDigits() {
        // Given
        String documentWithMask = "005.883.809-03";
        String normalizedDocument = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(normalizedDocument, transactionId))
                .thenReturn(expectedUser);

        // When
        controller.findByDocument(transactionId, documentWithMask);

        // Then
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(eq(normalizedDocument), eq(transactionId));
    }

    @Test
    @DisplayName("When finding user by document with spaces Then should remove spaces")
    void whenFindingUserByDocumentWithSpaces_thenShouldRemoveSpaces() {
        // Given
        String documentWithSpaces = "005 883 809 03";
        String normalizedDocument = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(normalizedDocument, transactionId))
                .thenReturn(expectedUser);

        // When
        controller.findByDocument(transactionId, documentWithSpaces);

        // Then
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(eq(normalizedDocument), eq(transactionId));
    }

    @Test
    @DisplayName("When finding non-existent user Then should throw UserNotFoundException")
    void whenFindingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenThrow(new UserNotFoundException("USER-404", "User not found"));

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            controller.findByDocument(transactionId, document);
        });

        assertEquals("USER-404", exception.getCode());
        assertEquals("User not found", exception.getMessage());

        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When use case throws exception Then should propagate exception")
    void whenUseCaseThrowsException_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.findByDocument(transactionId, document);
        });

        assertEquals("Database error", exception.getMessage());

        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When finding user with different transaction ids Then should use correct transaction id")
    void whenFindingUserWithDifferentTransactionIds_thenShouldUseCorrectTransactionId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(eq(document), any(UUID.class)))
                .thenReturn(expectedUser);

        // When
        controller.findByDocument(transactionId1, document);
        controller.findByDocument(transactionId2, document);

        // Then
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document, transactionId1);
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document, transactionId2);
    }

    @Test
    @DisplayName("When finding user Then should return response with user body")
    void whenFindingUser_thenShouldReturnResponseWithUserBody() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<User> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertSame(expectedUser, response.getBody());
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

        when(findUserByDocumentUseCase.findUserByDocument(document1, transactionId))
                .thenReturn(user1);
        when(findUserByDocumentUseCase.findUserByDocument(document2, transactionId))
                .thenReturn(user2);

        // When
        ResponseEntity<User> response1 = controller.findByDocument(transactionId, document1);
        ResponseEntity<User> response2 = controller.findByDocument(transactionId, document2);

        // Then
        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());
        assertEquals("User 1", response1.getBody().getFullName());
        assertEquals("User 2", response2.getBody().getFullName());
        assertEquals(document1, response1.getBody().getDocument());
        assertEquals(document2, response2.getBody().getDocument());

        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document1, transactionId);
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(document2, transactionId);
    }

    @Test
    @DisplayName("When finding user with mixed special characters Then should normalize correctly")
    void whenFindingUserWithMixedSpecialCharacters_thenShouldNormalizeCorrectly() {
        // Given
        String documentWithMixed = "005.883.809-03 / # @";
        String normalizedDocument = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(normalizedDocument, transactionId))
                .thenReturn(expectedUser);

        // When
        controller.findByDocument(transactionId, documentWithMixed);

        // Then
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(eq(normalizedDocument), eq(transactionId));
    }

    @Test
    @DisplayName("When finding user Then should pass transaction id to use case")
    void whenFindingUser_thenShouldPassTransactionIdToUseCase() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        controller.findByDocument(transactionId, document);

        // Then
        verify(findUserByDocumentUseCase, times(1))
                .findUserByDocument(eq(document), eq(transactionId));
    }
}