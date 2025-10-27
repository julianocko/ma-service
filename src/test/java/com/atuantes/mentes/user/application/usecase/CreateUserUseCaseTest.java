package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.domain.mapper.CreateUserCommandToUser;
import com.atuantes.mentes.user.domain.service.UserInsertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Given CreateUserUseCase")
class CreateUserUseCaseTest {

    @Mock
    private CreateUserCommandToUser createUserCommandToUser;

    @Mock
    private UserInsertRepository userInsertRepository;

    private CreateUserUseCase useCase;
    private UUID transactionId;
    private CreateUserCommand validCommand;
    private User mappedUser;
    private User createdUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateUserUseCase(createUserCommandToUser, userInsertRepository);
        transactionId = UUID.randomUUID();

        validCommand = new CreateUserCommand();
        validCommand.setFullName("João Silva");
        validCommand.setDocument("00588380903");
        validCommand.setEmail("joao@test.com");
        validCommand.setPhone("11999999999");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);

        mappedUser = new User();
        mappedUser.setFullName("João Silva");
        mappedUser.setDocument("00588380903");
        mappedUser.setEmail("joao@test.com");
        mappedUser.setPhone("11999999999");
        mappedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        mappedUser.setCategory(Category.FATHER);

        createdUser = new User();
        createdUser.setId(UUID.randomUUID());
        createdUser.setFullName("João Silva");
        createdUser.setActive(true);
        createdUser.setDocument("00588380903");
        createdUser.setEmail("joao@test.com");
        createdUser.setPhone("11999999999");
        createdUser.setBirthdate(LocalDate.of(1990, 1, 1));
        createdUser.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When creating user with valid command Then should return created user")
    void whenCreatingUserWithValidCommand_thenShouldReturnCreatedUser() {
        // Given
        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        User result = useCase.createUser(validCommand, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("João Silva", result.getFullName());
        assertEquals("00588380903", result.getDocument());
        assertEquals("joao@test.com", result.getEmail());
        assertEquals("11999999999", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertEquals(Category.FATHER, result.getCategory());
        assertTrue(result.isActive());

        verify(createUserCommandToUser, times(1)).toUser(validCommand);
        verify(userInsertRepository, times(1)).insert(mappedUser, transactionId);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When creating user with each category Then should process correctly")
    void whenCreatingUserWithEachCategory_thenShouldProcessCorrectly(Category category) {
        // Given
        validCommand.setCategory(category);
        mappedUser.setCategory(category);
        createdUser.setCategory(category);

        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        User result = useCase.createUser(validCommand, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(category, result.getCategory());
        verify(createUserCommandToUser, times(1)).toUser(validCommand);
        verify(userInsertRepository, times(1)).insert(mappedUser, transactionId);
    }

    @Test
    @DisplayName("When mapper throws UserIllegalArgumentException Then should propagate exception")
    void whenMapperThrowsUserIllegalArgumentException_thenShouldPropagateException() {
        // Given
        UserIllegalArgumentException exception = new UserIllegalArgumentException("USER-001", "Invalid document");
        when(createUserCommandToUser.toUser(validCommand)).thenThrow(exception);

        // When & Then
        UserIllegalArgumentException thrown = assertThrows(UserIllegalArgumentException.class, () -> {
            useCase.createUser(validCommand, transactionId);
        });

        assertEquals("USER-001", thrown.getCode());
        assertEquals("Invalid document", thrown.getMessage());
        verify(createUserCommandToUser, times(1)).toUser(validCommand);
        verify(userInsertRepository, never()).insert(any(), any());
    }

    @Test
    @DisplayName("When repository throws exception Then should propagate exception")
    void whenRepositoryThrowsException_thenShouldPropagateException() {
        // Given
        RuntimeException exception = new RuntimeException("Database error");
        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenThrow(exception);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            useCase.createUser(validCommand, transactionId);
        });

        assertEquals("Database error", thrown.getMessage());
        verify(createUserCommandToUser, times(1)).toUser(validCommand);
        verify(userInsertRepository, times(1)).insert(mappedUser, transactionId);
    }

    @Test
    @DisplayName("When creating user Then should call mapper before repository")
    void whenCreatingUser_thenShouldCallMapperBeforeRepository() {
        // Given
        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        useCase.createUser(validCommand, transactionId);

        // Then
        var inOrder = inOrder(createUserCommandToUser, userInsertRepository);
        inOrder.verify(createUserCommandToUser).toUser(validCommand);
        inOrder.verify(userInsertRepository).insert(mappedUser, transactionId);
    }

    @Test
    @DisplayName("When creating user with different transaction ids Then should use correct id")
    void whenCreatingUserWithDifferentTransactionIds_thenShouldUseCorrectId() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();

        when(createUserCommandToUser.toUser(any())).thenReturn(mappedUser);
        when(userInsertRepository.insert(any(), any())).thenReturn(createdUser);

        // When
        useCase.createUser(validCommand, transactionId1);
        useCase.createUser(validCommand, transactionId2);

        // Then
        verify(userInsertRepository, times(1)).insert(mappedUser, transactionId1);
        verify(userInsertRepository, times(1)).insert(mappedUser, transactionId2);
    }

    @Test
    @DisplayName("When creating multiple users Then should process each independently")
    void whenCreatingMultipleUsers_thenShouldProcessEachIndependently() {
        // Given
        CreateUserCommand command1 = new CreateUserCommand();
        command1.setFullName("User 1");
        command1.setDocument("00588380903");
        command1.setEmail("user1@test.com");
        command1.setPhone("11999999999");
        command1.setBirthdate(LocalDate.of(1990, 1, 1));
        command1.setCategory(Category.FATHER);

        CreateUserCommand command2 = new CreateUserCommand();
        command2.setFullName("User 2");
        command2.setDocument("00588380903");
        command2.setEmail("user2@test.com");
        command2.setPhone("11988888888");
        command2.setBirthdate(LocalDate.of(1995, 5, 15));
        command2.setCategory(Category.MOTHER);

        User mappedUser1 = new User();
        mappedUser1.setFullName("User 1");
        mappedUser1.setDocument("00588380903");
        mappedUser1.setEmail("user1@test.com");
        mappedUser1.setPhone("11999999999");
        mappedUser1.setBirthdate(LocalDate.of(1990, 1, 1));
        mappedUser1.setCategory(Category.FATHER);

        User mappedUser2 = new User();
        mappedUser2.setFullName("User 2");
        mappedUser2.setDocument("00588380903");
        mappedUser2.setEmail("user2@test.com");
        mappedUser2.setPhone("11988888888");
        mappedUser2.setBirthdate(LocalDate.of(1995, 5, 15));
        mappedUser2.setCategory(Category.MOTHER);

        User createdUser1 = new User();
        createdUser1.setId(UUID.randomUUID());
        createdUser1.setFullName("User 1");
        createdUser1.setActive(true);
        createdUser1.setDocument("00588380903");
        createdUser1.setEmail("user1@test.com");
        createdUser1.setPhone("11999999999");
        createdUser1.setBirthdate(LocalDate.of(1990, 1, 1));
        createdUser1.setCategory(Category.FATHER);

        User createdUser2 = new User();
        createdUser2.setId(UUID.randomUUID());
        createdUser2.setFullName("User 2");
        createdUser2.setActive(true);
        createdUser2.setDocument("00588380903");
        createdUser2.setEmail("user2@test.com");
        createdUser2.setPhone("11988888888");
        createdUser2.setBirthdate(LocalDate.of(1995, 5, 15));
        createdUser2.setCategory(Category.MOTHER);

        when(createUserCommandToUser.toUser(command1)).thenReturn(mappedUser1);
        when(createUserCommandToUser.toUser(command2)).thenReturn(mappedUser2);
        when(userInsertRepository.insert(mappedUser1, transactionId)).thenReturn(createdUser1);
        when(userInsertRepository.insert(mappedUser2, transactionId)).thenReturn(createdUser2);

        // When
        User result1 = useCase.createUser(command1, transactionId);
        User result2 = useCase.createUser(command2, transactionId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals(Category.FATHER, result1.getCategory());
        assertEquals(Category.MOTHER, result2.getCategory());

        verify(createUserCommandToUser, times(1)).toUser(command1);
        verify(createUserCommandToUser, times(1)).toUser(command2);
        verify(userInsertRepository, times(1)).insert(mappedUser1, transactionId);
        verify(userInsertRepository, times(1)).insert(mappedUser2, transactionId);
    }

    @Test
    @DisplayName("When repository returns user with id Then should return user with id")
    void whenRepositoryReturnsUserWithId_thenShouldReturnUserWithId() {
        // Given
        UUID expectedId = UUID.randomUUID();
        createdUser.setId(expectedId);

        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        User result = useCase.createUser(validCommand, transactionId);

        // Then
        assertNotNull(result.getId());
        assertEquals(expectedId, result.getId());
    }

    @Test
    @DisplayName("When creating user Then should pass mapped user to repository")
    void whenCreatingUser_thenShouldPassMappedUserToRepository() {
        // Given
        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        useCase.createUser(validCommand, transactionId);

        // Then
        verify(userInsertRepository, times(1)).insert(eq(mappedUser), eq(transactionId));
    }

    @Test
    @DisplayName("When use case is created Then should have non-null dependencies")
    void whenUseCaseIsCreated_thenShouldHaveNonNullDependencies() {
        // When & Then
        assertNotNull(useCase.createUserCommandToUser());
        assertNotNull(useCase.userInsertRepository());
    }

    @Test
    @DisplayName("When creating user with special characters Then should handle correctly")
    void whenCreatingUserWithSpecialCharacters_thenShouldHandleCorrectly() {
        // Given
        validCommand.setFullName("José María Ñoño de Souza");
        mappedUser.setFullName("José María Ñoño de Souza");
        createdUser.setFullName("José María Ñoño de Souza");

        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        User result = useCase.createUser(validCommand, transactionId);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());
    }

    @Test
    @DisplayName("When creating user with birthdate today Then should process correctly")
    void whenCreatingUserWithBirthdateToday_thenShouldProcessCorrectly() {
        // Given
        LocalDate today = LocalDate.now();
        validCommand.setBirthdate(today);
        mappedUser.setBirthdate(today);
        createdUser.setBirthdate(today);

        when(createUserCommandToUser.toUser(validCommand)).thenReturn(mappedUser);
        when(userInsertRepository.insert(mappedUser, transactionId)).thenReturn(createdUser);

        // When
        User result = useCase.createUser(validCommand, transactionId);

        // Then
        assertEquals(today, result.getBirthdate());
    }
}