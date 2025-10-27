package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserPersistenceException;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Given UserInsertRepostImpl")
class UserInsertRepostImplTest {

    @Mock
    private UserRepository userRepository;

    private UserInsertRepostImpl userInsertRepostImpl;

    @BeforeEach
    void setUp() {
        userInsertRepostImpl = new UserInsertRepostImpl(userRepository);
    }

    @Test
    @DisplayName("When inserting a valid user Then should call repository and return persisted user")
    void whenInsertingValidUser_thenShouldCallRepositoryAndReturnPersistedUser() {
        // Given
        UUID transactionId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("João Silva");
        user.setActive(Boolean.TRUE);
        user.setDocument("12345678900");
        user.setEmail("joao@test.com");
        user.setPhone("11999999999");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCategory(Category.FATHER);

        User expectedUser = new User();
        expectedUser.setId(UUID.randomUUID());
        expectedUser.setFullName("João Silva");
        expectedUser.setActive(Boolean.TRUE);
        expectedUser.setDocument("12345678900");
        expectedUser.setEmail("joao@test.com");
        expectedUser.setPhone("11999999999");
        expectedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        expectedUser.setCategory(Category.FATHER);

        when(userRepository.save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenReturn(expectedUser);

        // When
        User result = userInsertRepostImpl.insert(user, transactionId);

        // Then
        assertNotNull(result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getFullName(), result.getFullName());
        assertEquals(expectedUser.getDocument(), result.getDocument());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getPhone(), result.getPhone());
        assertEquals(expectedUser.getBirthdate(), result.getBirthdate());
        assertEquals(expectedUser.getCategory(), result.getCategory());

        verify(userRepository, times(1))
                .save(user.getFullName(), user.getDocument(), user.getEmail(),
                        user.getPhone(), user.getBirthdate(), user.getCategory().name());
    }

    @Test
    @DisplayName("When inserting user with duplicate document Then should throw UserPersistenceException")
    void whenInsertingUserWithDuplicateDocument_thenShouldThrowUserPersistenceException() {
        // Given
        UUID transactionId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("João Silva");
        user.setActive(Boolean.TRUE);
        user.setDocument("12345678900");
        user.setEmail("joao@test.com");
        user.setPhone("11999999999");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCategory(Category.FATHER);

        when(userRepository.save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenThrow(new DuplicateKeyException("Duplicate document"));

        // When & Then
        UserPersistenceException exception = assertThrows(UserPersistenceException.class,
                () -> userInsertRepostImpl.insert(user, transactionId));

        assertEquals(UserErrorMessage.DUPLICATE_DOCUMENT_ERROR.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.DUPLICATE_DOCUMENT_ERROR.getMessage(), exception.getMessage());

        verify(userRepository, times(1))
                .save(user.getFullName(), user.getDocument(), user.getEmail(),
                        user.getPhone(), user.getBirthdate(), user.getCategory().name());
    }

    @Test
    @DisplayName("When repository throws generic exception Then should throw UserPersistenceException")
    void whenRepositoryThrowsGenericException_thenShouldThrowUserPersistenceException() {
        // Given
        UUID transactionId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("João Silva");
        user.setActive(Boolean.TRUE);
        user.setDocument("12345678900");
        user.setEmail("joao@test.com");
        user.setPhone("11999999999");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCategory(Category.FATHER);

        when(userRepository.save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThrows(UserPersistenceException.class,
                () -> userInsertRepostImpl.insert(user, transactionId));

        verify(userRepository, times(1))
                .save(user.getFullName(), user.getDocument(), user.getEmail(),
                        user.getPhone(), user.getBirthdate(), user.getCategory().name());
    }

    @Test
    @DisplayName("When inserting users with all categories Then should handle all correctly")
    void whenInsertingUsersWithAllCategories_thenShouldHandleAllCorrectly() {
        // Given
        UUID transactionId = UUID.randomUUID();

        for (Category category : Category.values()) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setFullName("Test User");
            user.setActive(Boolean.TRUE);
            user.setDocument("12345678900");
            user.setEmail("test@test.com");
            user.setPhone("11999999999");
            user.setBirthdate(LocalDate.of(1990, 1, 1));
            user.setCategory(category);

            User expectedUser = new User();
            expectedUser.setId(UUID.randomUUID());
            expectedUser.setFullName("Test User");
            expectedUser.setActive(Boolean.TRUE);
            expectedUser.setDocument("12345678900");
            expectedUser.setEmail("test@test.com");
            expectedUser.setPhone("11999999999");
            expectedUser.setBirthdate(LocalDate.of(1990, 1, 1));
            expectedUser.setCategory(category);

            when(userRepository.save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), eq(category.name())))
                    .thenReturn(expectedUser);

            // When
            User result = userInsertRepostImpl.insert(user, transactionId);

            // Then
            assertNotNull(result);
            assertEquals(category, result.getCategory());
        }

        verify(userRepository, times(Category.values().length))
                .save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), anyString());
    }

    @Test
    @DisplayName("When inserting user Then should pass correct parameters to repository")
    void whenInsertingUser_thenShouldPassCorrectParametersToRepository() {
        // Given
        UUID transactionId = UUID.randomUUID();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Maria Santos");
        user.setActive(Boolean.TRUE);
        user.setDocument("98765432100");
        user.setEmail("maria@test.com");
        user.setPhone("11988888888");
        user.setBirthdate(LocalDate.of(1985, 5, 15));
        user.setCategory(Category.MOTHER);

        User expectedUser = new User();
        expectedUser.setId(UUID.randomUUID());
        expectedUser.setFullName("Maria Santos");
        expectedUser.setActive(Boolean.TRUE);
        expectedUser.setDocument("98765432100");
        expectedUser.setEmail("maria@test.com");
        expectedUser.setPhone("11988888888");
        expectedUser.setBirthdate(LocalDate.of(1985, 5, 15));
        expectedUser.setCategory(Category.MOTHER);

        when(userRepository.save(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class), anyString()))
                .thenReturn(expectedUser);

        // When
        userInsertRepostImpl.insert(user, transactionId);

        // Then
        verify(userRepository, times(1))
                .save("Maria Santos", "98765432100", "maria@test.com",
                        "11988888888", LocalDate.of(1985, 5, 15), "MOTHER");
    }
}