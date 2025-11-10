package com.atuantes.mentes.user.domain.bootstrap;

import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.application.usecase.DeleteUserByDocumentUseCase;
import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.mapper.CreateUserCommandToUser;
import com.atuantes.mentes.user.domain.mapper.UpdateUserCommandToUser;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import com.atuantes.mentes.user.domain.service.UserDelete;
import com.atuantes.mentes.user.domain.service.UserInsert;
import com.atuantes.mentes.user.domain.service.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeanFactoryTest {

    private BeanFactory beanFactory;

    @Mock
    private CreateUserCommandToUser createUserCommandToUser;

    @Mock
    private UserInsert userInsert;

    @Mock
    private FindUserByDocument findUserByDocument;

    @Mock
    private UpdateUserCommandToUser updateUserCommandToUser;

    @Mock
    private UserUpdate userUpdate;

    @Mock
    private UserDelete userDelete;

    @BeforeEach
    void setUp() {
        beanFactory = new BeanFactory();
    }

    @Test
    @DisplayName("When creating CreateUserUseCase bean Then should return non-null instance")
    void whenCreatingCreateUserUseCaseBean_thenShouldReturnNonNullInstance() {
        // When
        CreateUserUseCase result = beanFactory.createUserUseCase(createUserCommandToUser, userInsert);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("When creating FindUserByDocumentUseCase bean Then should return non-null instance")
    void whenCreatingFindUserByDocumentUseCaseBean_thenShouldReturnNonNullInstance() {
        // When
        FindUserByDocumentUseCase result = beanFactory.findUserByDocumentUseCase(findUserByDocument);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("When creating UpdateUserUseCase bean Then should return non-null instance")
    void whenCreatingUpdateUserUseCaseBean_thenShouldReturnNonNullInstance() {
        // When
        UpdateUserUseCase result = beanFactory.updateUserUseCase(updateUserCommandToUser, userUpdate);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("When creating UpdateUserCommandToUser bean Then should return non-null instance")
    void whenCreatingUpdateUserCommandToUserBean_thenShouldReturnNonNullInstance() {
        // When
        UpdateUserCommandToUser result = beanFactory.updateUserCommandToUser();

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("When creating DeleteUserByDocumentUseCase bean Then should return non-null instance")
    void whenCreatingDeleteUserByDocumentUseCaseBean_thenShouldReturnNonNullInstance() {
        // When
        DeleteUserByDocumentUseCase result = beanFactory.deleteUserByDocumentUseCase(userDelete);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("When creating multiple CreateUserUseCase beans Then should return different instances")
    void whenCreatingMultipleCreateUserUseCaseBeans_thenShouldReturnDifferentInstances() {
        // When
        CreateUserUseCase result1 = beanFactory.createUserUseCase(createUserCommandToUser, userInsert);
        CreateUserUseCase result2 = beanFactory.createUserUseCase(createUserCommandToUser, userInsert);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

    @Test
    @DisplayName("When creating multiple FindUserByDocumentUseCase beans Then should return different instances")
    void whenCreatingMultipleFindUserByDocumentUseCaseBeans_thenShouldReturnDifferentInstances() {
        // When
        FindUserByDocumentUseCase result1 = beanFactory.findUserByDocumentUseCase(findUserByDocument);
        FindUserByDocumentUseCase result2 = beanFactory.findUserByDocumentUseCase(findUserByDocument);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

    @Test
    @DisplayName("When creating multiple UpdateUserUseCase beans Then should return different instances")
    void whenCreatingMultipleUpdateUserUseCaseBeans_thenShouldReturnDifferentInstances() {
        // When
        UpdateUserUseCase result1 = beanFactory.updateUserUseCase(updateUserCommandToUser, userUpdate);
        UpdateUserUseCase result2 = beanFactory.updateUserUseCase(updateUserCommandToUser, userUpdate);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

    @Test
    @DisplayName("When creating multiple UpdateUserCommandToUser beans Then should return different instances")
    void whenCreatingMultipleUpdateUserCommandToUserBeans_thenShouldReturnDifferentInstances() {
        // When
        UpdateUserCommandToUser result1 = beanFactory.updateUserCommandToUser();
        UpdateUserCommandToUser result2 = beanFactory.updateUserCommandToUser();

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

    @Test
    @DisplayName("When creating multiple DeleteUserByDocumentUseCase beans Then should return different instances")
    void whenCreatingMultipleDeleteUserByDocumentUseCaseBeans_thenShouldReturnDifferentInstances() {
        // When
        DeleteUserByDocumentUseCase result1 = beanFactory.deleteUserByDocumentUseCase(userDelete);
        DeleteUserByDocumentUseCase result2 = beanFactory.deleteUserByDocumentUseCase(userDelete);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }

    @Test
    @DisplayName("When BeanFactory is created Then should not be null")
    void whenBeanFactoryIsCreated_thenShouldNotBeNull() {
        // When & Then
        assertNotNull(beanFactory);
    }

    @Test
    @DisplayName("When creating CreateUserUseCase with dependencies Then should accept valid parameters")
    void whenCreatingCreateUserUseCaseWithDependencies_thenShouldAcceptValidParameters() {
        // When
        CreateUserUseCase result = beanFactory.createUserUseCase(createUserCommandToUser, userInsert);

        // Then
        assertNotNull(result);
        assertInstanceOf(CreateUserUseCase.class, result);
    }

    @Test
    @DisplayName("When creating FindUserByDocumentUseCase with dependencies Then should accept valid parameters")
    void whenCreatingFindUserByDocumentUseCaseWithDependencies_thenShouldAcceptValidParameters() {
        // When
        FindUserByDocumentUseCase result = beanFactory.findUserByDocumentUseCase(findUserByDocument);

        // Then
        assertNotNull(result);
        assertInstanceOf(FindUserByDocumentUseCase.class, result);
    }

    @Test
    @DisplayName("When creating UpdateUserUseCase with dependencies Then should accept valid parameters")
    void whenCreatingUpdateUserUseCaseWithDependencies_thenShouldAcceptValidParameters() {
        // When
        UpdateUserUseCase result = beanFactory.updateUserUseCase(updateUserCommandToUser, userUpdate);

        // Then
        assertNotNull(result);
        assertInstanceOf(UpdateUserUseCase.class, result);
    }

    @Test
    @DisplayName("When creating DeleteUserByDocumentUseCase with dependencies Then should accept valid parameters")
    void whenCreatingDeleteUserByDocumentUseCaseWithDependencies_thenShouldAcceptValidParameters() {
        // When
        DeleteUserByDocumentUseCase result = beanFactory.deleteUserByDocumentUseCase(userDelete);

        // Then
        assertNotNull(result);
        assertInstanceOf(DeleteUserByDocumentUseCase.class, result);
    }
}