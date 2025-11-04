package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.DeleteUserByDocumentCommand;
import com.atuantes.mentes.user.application.usecase.DeleteUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given DeleteUserByDocumentController")
class DeleteUserByDocumentControllerTest {

    @Mock
    private DeleteUserByDocumentUseCase deleteUserByDocumentUseCase;

    private DeleteUserByDocumentController controller;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        controller = new DeleteUserByDocumentController(deleteUserByDocumentUseCase);
        transactionId = UUID.randomUUID();
    }

    @Test
    @DisplayName("When deleting user by valid document Then should return 204 No Content")
    void whenDeletingUserByValidDocument_thenShouldReturn204NoContent() {
        // Given
        String document = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        ResponseEntity<Void> response = controller.deleteByDocument(transactionId, document);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(document)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user by document with special characters Then should normalize and delete")
    void whenDeletingUserByDocumentWithSpecialCharacters_thenShouldNormalizeAndDelete() {
        // Given
        String documentWithMask = "005.883.809-03";
        String normalizedDocument = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        ResponseEntity<Void> response = controller.deleteByDocument(transactionId, documentWithMask);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(normalizedDocument)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user by document with dots and dashes Then should remove all non-digits")
    void whenDeletingUserByDocumentWithDotsAndDashes_thenShouldRemoveAllNonDigits() {
        // Given
        String documentWithMask = "005.883.809-03";
        String normalizedDocument = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        controller.deleteByDocument(transactionId, documentWithMask);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(normalizedDocument)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user by document with spaces Then should remove spaces")
    void whenDeletingUserByDocumentWithSpaces_thenShouldRemoveSpaces() {
        // Given
        String documentWithSpaces = "005 883 809 03";
        String normalizedDocument = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        controller.deleteByDocument(transactionId, documentWithSpaces);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(normalizedDocument)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting non-existent user Then should throw UserNotFoundException")
    void whenDeletingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        doThrow(new UserNotFoundException("USER-0007", "Usuário não encontrado para o documento informado."))
                .when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            controller.deleteByDocument(transactionId, document);
        });

        assertEquals("USER-0007", exception.getCode());
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));

        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(document)), eq(transactionId));
    }

    @Test
    @DisplayName("When use case throws exception Then should propagate exception")
    void whenUseCaseThrowsException_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        doThrow(new RuntimeException("Database error"))
                .when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            controller.deleteByDocument(transactionId, document);
        });

        assertEquals("Database error", exception.getMessage());

        verify(deleteUserByDocumentUseCase, times(1))
                .execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user with different transaction ids Then should use correct transaction id")
    void whenDeletingUserWithDifferentTransactionIds_thenShouldUseCorrectTransactionId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), any(UUID.class));

        // When
        controller.deleteByDocument(transactionId1, document);
        controller.deleteByDocument(transactionId2, document);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(any(DeleteUserByDocumentCommand.class), eq(transactionId1));
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(any(DeleteUserByDocumentCommand.class), eq(transactionId2));
    }

    @Test
    @DisplayName("When deleting user Then should return response with no body")
    void whenDeletingUser_thenShouldReturnResponseWithNoBody() {
        // Given
        String document = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        ResponseEntity<Void> response = controller.deleteByDocument(transactionId, document);

        // Then
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("When deleting multiple users by different documents Then should process each independently")
    void whenDeletingMultipleUsersByDifferentDocuments_thenShouldProcessEachIndependently() {
        // Given
        String document1 = "00588380903";
        String document2 = "98765432100";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        ResponseEntity<Void> response1 = controller.deleteByDocument(transactionId, document1);
        ResponseEntity<Void> response2 = controller.deleteByDocument(transactionId, document2);

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(HttpStatus.NO_CONTENT, response1.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, response2.getStatusCode());

        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(document1)), eq(transactionId));
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(document2)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user with mixed special characters Then should normalize correctly")
    void whenDeletingUserWithMixedSpecialCharacters_thenShouldNormalizeCorrectly() {
        // Given
        String documentWithMixed = "005.883.809-03 / # @";
        String normalizedDocument = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        controller.deleteByDocument(transactionId, documentWithMixed);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> cmd.getDocument().equals(normalizedDocument)), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user Then should pass transaction id to use case")
    void whenDeletingUser_thenShouldPassTransactionIdToUseCase() {
        // Given
        String document = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        controller.deleteByDocument(transactionId, document);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));
    }

    @Test
    @DisplayName("When deleting user Then should create command with normalized document")
    void whenDeletingUser_thenShouldCreateCommandWithNormalizedDocument() {
        // Given
        String document = "005.883.809-03";
        String normalizedDocument = "00588380903";
        doNothing().when(deleteUserByDocumentUseCase).execute(any(DeleteUserByDocumentCommand.class), eq(transactionId));

        // When
        controller.deleteByDocument(transactionId, document);

        // Then
        verify(deleteUserByDocumentUseCase, times(1))
                .execute(argThat(cmd -> 
                    cmd != null && 
                    cmd.getDocument() != null && 
                    cmd.getDocument().equals(normalizedDocument)
                ), eq(transactionId));
    }
}
