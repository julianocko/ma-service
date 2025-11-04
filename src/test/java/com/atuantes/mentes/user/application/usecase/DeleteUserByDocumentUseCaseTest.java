package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.command.DeleteUserByDocumentCommand;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.service.UserDelete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given DeleteUserByDocumentUseCase")
class DeleteUserByDocumentUseCaseTest {

    @Mock
    private UserDelete userDelete;

    private DeleteUserByDocumentUseCase useCase;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserByDocumentUseCase(userDelete);
        transactionId = UUID.randomUUID();
    }

    @Test
    @DisplayName("When executing with valid command Then should delegate to UserDelete")
    void whenExecutingWithValidCommand_thenShouldDelegateToUserDelete() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doNothing().when(userDelete).deleteByDocument(document, transactionId);

        // When
        useCase.execute(command, transactionId);

        // Then
        verify(userDelete, times(1)).deleteByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When user does not exist Then should throw UserNotFoundException")
    void whenUserDoesNotExist_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doThrow(new UserNotFoundException("USER-0007", "Usuário não encontrado para o documento informado."))
                .when(userDelete).deleteByDocument(document, transactionId);

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            useCase.execute(command, transactionId);
        });

        assertEquals("USER-0007", exception.getCode());
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));

        verify(userDelete, times(1)).deleteByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When service throws exception Then should propagate exception")
    void whenServiceThrowsException_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doThrow(new RuntimeException("Database error"))
                .when(userDelete).deleteByDocument(document, transactionId);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.execute(command, transactionId);
        });

        assertEquals("Database error", exception.getMessage());

        verify(userDelete, times(1)).deleteByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When executing with different transaction ids Then should use correct transaction id")
    void whenExecutingWithDifferentTransactionIds_thenShouldUseCorrectTransactionId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doNothing().when(userDelete).deleteByDocument(eq(document), any(UUID.class));

        // When
        useCase.execute(command, transactionId1);
        useCase.execute(command, transactionId2);

        // Then
        verify(userDelete, times(1)).deleteByDocument(document, transactionId1);
        verify(userDelete, times(1)).deleteByDocument(document, transactionId2);
    }

    @Test
    @DisplayName("When executing multiple commands with different documents Then should process each independently")
    void whenExecutingMultipleCommandsWithDifferentDocuments_thenShouldProcessEachIndependently() {
        // Given
        String document1 = "00588380903";
        String document2 = "98765432100";
        DeleteUserByDocumentCommand command1 = new DeleteUserByDocumentCommand(document1);
        DeleteUserByDocumentCommand command2 = new DeleteUserByDocumentCommand(document2);
        doNothing().when(userDelete).deleteByDocument(anyString(), eq(transactionId));

        // When
        useCase.execute(command1, transactionId);
        useCase.execute(command2, transactionId);

        // Then
        verify(userDelete, times(1)).deleteByDocument(document1, transactionId);
        verify(userDelete, times(1)).deleteByDocument(document2, transactionId);
    }

    @Test
    @DisplayName("When executing Then should pass correct parameters to service")
    void whenExecuting_thenShouldPassCorrectParametersToService() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doNothing().when(userDelete).deleteByDocument(document, transactionId);

        // When
        useCase.execute(command, transactionId);

        // Then
        verify(userDelete, times(1)).deleteByDocument(eq(document), eq(transactionId));
    }

    @Test
    @DisplayName("When use case is created Then should have non-null dependency")
    void whenUseCaseIsCreated_thenShouldHaveNonNullDependency() {
        // When & Then
        assertNotNull(useCase.userDelete());
    }

    @Test
    @DisplayName("When executing command Then should not return any value")
    void whenExecutingCommand_thenShouldNotReturnAnyValue() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doNothing().when(userDelete).deleteByDocument(document, transactionId);

        // When
        useCase.execute(command, transactionId);

        // Then - no exception thrown and void method completes
        verify(userDelete, times(1)).deleteByDocument(document, transactionId);
    }

    @Test
    @DisplayName("When executing with null document in command Then should pass null to service")
    void whenExecutingWithNullDocumentInCommand_thenShouldPassNullToService() {
        // Given
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(null);
        doNothing().when(userDelete).deleteByDocument(null, transactionId);

        // When
        useCase.execute(command, transactionId);

        // Then
        verify(userDelete, times(1)).deleteByDocument(null, transactionId);
    }

    @Test
    @DisplayName("When executing multiple times Then should call service each time")
    void whenExecutingMultipleTimes_thenShouldCallServiceEachTime() {
        // Given
        String document = "00588380903";
        DeleteUserByDocumentCommand command = new DeleteUserByDocumentCommand(document);
        doNothing().when(userDelete).deleteByDocument(document, transactionId);

        // When
        useCase.execute(command, transactionId);
        useCase.execute(command, transactionId);
        useCase.execute(command, transactionId);

        // Then
        verify(userDelete, times(3)).deleteByDocument(document, transactionId);
    }
}
