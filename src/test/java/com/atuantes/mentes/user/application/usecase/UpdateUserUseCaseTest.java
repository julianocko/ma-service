package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.mapper.UpdateUserCommandToUser;
import com.atuantes.mentes.user.domain.service.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given UpdateUserUseCase")
class UpdateUserUseCaseTest {

    @Mock
    private UpdateUserCommandToUser updateUserCommandToUser;

    @Mock
    private UserUpdate userUpdate;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private UpdateUserCommand validCommand;
    private User validUser;
    private User updatedUser;
    private UUID transactionId;

    @BeforeEach
    void setUp() {
        validCommand = new UpdateUserCommand();
        validCommand.setDocument("00588380903");
        validCommand.setFullName("João Silva");
        validCommand.setEmail("joao@test.com");
        validCommand.setPhone("11999999999");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);
        validCommand.setActive(true);

        validUser = new User();
        validUser.setDocument("00588380903");
        validUser.setFullName("João Silva");
        validUser.setEmail("joao@test.com");
        validUser.setPhone("11999999999");
        validUser.setBirthdate(LocalDate.of(1990, 1, 1));
        validUser.setCategory(Category.FATHER);
        validUser.setActive(true);

        updatedUser = new User();
        updatedUser.setId(UUID.randomUUID());
        updatedUser.setDocument("00588380903");
        updatedUser.setFullName("João Silva");
        updatedUser.setEmail("joao@test.com");
        updatedUser.setPhone("11999999999");
        updatedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        updatedUser.setCategory(Category.FATHER);
        updatedUser.setActive(true);

        transactionId = UUID.randomUUID();
    }

    @Test
    @DisplayName("When updating user with valid command Then should return updated user")
    void whenUpdatingUserWithValidCommand_thenShouldReturnUpdatedUser() {
        // Given
        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId)).thenReturn(updatedUser);

        // When
        User result = updateUserUseCase.updateUser(validCommand, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getDocument(), result.getDocument());
        assertEquals(updatedUser.getFullName(), result.getFullName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getPhone(), result.getPhone());
        assertEquals(updatedUser.getBirthdate(), result.getBirthdate());
        assertEquals(updatedUser.getCategory(), result.getCategory());
        assertEquals(updatedUser.isActive(), result.isActive());

        verify(updateUserCommandToUser, times(1)).toUser(validCommand);
        verify(userUpdate, times(1)).update(validUser, transactionId);
    }

    @Test
    @DisplayName("When updating user Then should call mapper and service in correct order")
    void whenUpdatingUser_thenShouldCallMapperAndServiceInCorrectOrder() {
        // Given
        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId)).thenReturn(updatedUser);

        // When
        updateUserUseCase.updateUser(validCommand, transactionId);

        // Then
        var inOrder = inOrder(updateUserCommandToUser, userUpdate);
        inOrder.verify(updateUserCommandToUser).toUser(validCommand);
        inOrder.verify(userUpdate).update(validUser, transactionId);
    }

    @Test
    @DisplayName("When mapper throws exception Then should propagate exception")
    void whenMapperThrowsException_thenShouldPropagateException() {
        // Given
        UserIllegalArgumentException exception = new UserIllegalArgumentException(
                "USER-UPDATE-002",
                "Erro ao mapear UpdateUserCommand para User"
        );
        when(updateUserCommandToUser.toUser(validCommand)).thenThrow(exception);

        // When & Then
        UserIllegalArgumentException thrown = assertThrows(UserIllegalArgumentException.class, () -> {
            updateUserUseCase.updateUser(validCommand, transactionId);
        });

        assertEquals("USER-UPDATE-002", thrown.getCode());
        assertEquals("Erro ao mapear UpdateUserCommand para User", thrown.getMessage());

        verify(updateUserCommandToUser, times(1)).toUser(validCommand);
        verify(userUpdate, never()).update(any(), any());
    }

    @Test
    @DisplayName("When service throws exception Then should propagate exception")
    void whenServiceThrowsException_thenShouldPropagateException() {
        // Given
        UserIllegalArgumentException exception = new UserIllegalArgumentException(
                "USER-UPDATE-003",
                "Erro ao atualizar usuário"
        );
        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId)).thenThrow(exception);

        // When & Then
        UserIllegalArgumentException thrown = assertThrows(UserIllegalArgumentException.class, () -> {
            updateUserUseCase.updateUser(validCommand, transactionId);
        });

        assertEquals("USER-UPDATE-003", thrown.getCode());
        assertEquals("Erro ao atualizar usuário", thrown.getMessage());

        verify(updateUserCommandToUser, times(1)).toUser(validCommand);
        verify(userUpdate, times(1)).update(validUser, transactionId);
    }

    @Test
    @DisplayName("When updating user with different transaction ids Then should use correct transaction id")
    void whenUpdatingUserWithDifferentTransactionIds_thenShouldUseCorrectTransactionId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();

        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId1)).thenReturn(updatedUser);
        when(userUpdate.update(validUser, transactionId2)).thenReturn(updatedUser);

        // When
        updateUserUseCase.updateUser(validCommand, transactionId1);
        updateUserUseCase.updateUser(validCommand, transactionId2);

        // Then
        verify(userUpdate, times(1)).update(validUser, transactionId1);
        verify(userUpdate, times(1)).update(validUser, transactionId2);
    }

    @Test
    @DisplayName("When updating user with active false Then should process correctly")
    void whenUpdatingUserWithActiveFalse_thenShouldProcessCorrectly() {
        // Given
        validCommand.setActive(false);
        validUser.setActive(false);
        updatedUser.setActive(false);

        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId)).thenReturn(updatedUser);

        // When
        User result = updateUserUseCase.updateUser(validCommand, transactionId);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(updateUserCommandToUser, times(1)).toUser(validCommand);
        verify(userUpdate, times(1)).update(validUser, transactionId);
    }

    @Test
    @DisplayName("When updating user with different category Then should process correctly")
    void whenUpdatingUserWithDifferentCategory_thenShouldProcessCorrectly() {
        // Given
        validCommand.setCategory(Category.MOTHER);
        validUser.setCategory(Category.MOTHER);
        updatedUser.setCategory(Category.MOTHER);

        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(validUser, transactionId)).thenReturn(updatedUser);

        // When
        User result = updateUserUseCase.updateUser(validCommand, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(Category.MOTHER, result.getCategory());
        verify(updateUserCommandToUser, times(1)).toUser(validCommand);
        verify(userUpdate, times(1)).update(validUser, transactionId);
    }

    @Test
    @DisplayName("When updating multiple users Then should process each independently")
    void whenUpdatingMultipleUsers_thenShouldProcessEachIndependently() {
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

        User user1 = new User();
        user1.setDocument("00588380903");
        user1.setFullName("User 1");

        User user2 = new User();
        user2.setDocument("98765432100");
        user2.setFullName("User 2");

        when(updateUserCommandToUser.toUser(command1)).thenReturn(user1);
        when(updateUserCommandToUser.toUser(command2)).thenReturn(user2);
        when(userUpdate.update(user1, transactionId)).thenReturn(user1);
        when(userUpdate.update(user2, transactionId)).thenReturn(user2);

        // When
        User result1 = updateUserUseCase.updateUser(command1, transactionId);
        User result2 = updateUserUseCase.updateUser(command2, transactionId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());

        verify(updateUserCommandToUser, times(1)).toUser(command1);
        verify(updateUserCommandToUser, times(1)).toUser(command2);
        verify(userUpdate, times(1)).update(user1, transactionId);
        verify(userUpdate, times(1)).update(user2, transactionId);
    }

    @Test
    @DisplayName("When updating user Then should pass correct user object to service")
    void whenUpdatingUser_thenShouldPassCorrectUserObjectToService() {
        // Given
        when(updateUserCommandToUser.toUser(validCommand)).thenReturn(validUser);
        when(userUpdate.update(eq(validUser), eq(transactionId))).thenReturn(updatedUser);

        // When
        updateUserUseCase.updateUser(validCommand, transactionId);

        // Then
        verify(userUpdate, times(1)).update(eq(validUser), eq(transactionId));
    }

    @Test
    @DisplayName("When record is instantiated Then should have correct dependencies")
    void whenRecordIsInstantiated_thenShouldHaveCorrectDependencies() {
        // When
        UpdateUserUseCase useCase = new UpdateUserUseCase(updateUserCommandToUser, userUpdate);

        // Then
        assertNotNull(useCase);
        assertEquals(updateUserCommandToUser, useCase.updateUserCommandToUser());
        assertEquals(userUpdate, useCase.userUpdate());
    }
}