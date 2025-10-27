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
@DisplayName("Given FindUserByDocumentImpl")
class FindUserByDocumentImplTest {

    @Mock
    private UserRepository userRepository;

    private FindUserByDocumentImpl findUserByDocument;
    private UUID transactionId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        findUserByDocument = new FindUserByDocumentImpl(userRepository);
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
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        User result = findUserByDocument.execute(document, transactionId);

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

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When finding non-existent user Then should throw UserNotFoundException")
    void whenFindingNonExistentUser_thenShouldThrowUserNotFoundException() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            findUserByDocument.execute(document, transactionId);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When repository throws exception Then should propagate exception")
    void whenRepositoryThrowsException_thenShouldPropagateException() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            findUserByDocument.execute(document, transactionId);
        });

        assertEquals("Database error", exception.getMessage());

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When finding user with different transaction ids Then should find user independently")
    void whenFindingUserWithDifferentTransactionIds_thenShouldFindUserIndependently() {
        // Given
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        User result1 = findUserByDocument.execute(document, transactionId1);
        User result2 = findUserByDocument.execute(document, transactionId2);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(expectedUser.getId(), result1.getId());
        assertEquals(expectedUser.getId(), result2.getId());

        verify(userRepository, times(2)).findByDocument(document);
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

        when(userRepository.findByDocument(document1)).thenReturn(Optional.of(user1));
        when(userRepository.findByDocument(document2)).thenReturn(Optional.of(user2));

        // When
        User result1 = findUserByDocument.execute(document1, transactionId);
        User result2 = findUserByDocument.execute(document2, transactionId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
        assertEquals("User 1", result1.getFullName());
        assertEquals("User 2", result2.getFullName());
        assertEquals(document1, result1.getDocument());
        assertEquals(document2, result2.getDocument());

        verify(userRepository, times(1)).findByDocument(document1);
        verify(userRepository, times(1)).findByDocument(document2);
    }

    @Test
    @DisplayName("When finding inactive user Then should return inactive user")
    void whenFindingInactiveUser_thenShouldReturnInactiveUser() {
        // Given
        String document = "00588380903";
        expectedUser.setActive(false);
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        User result = findUserByDocument.execute(document, transactionId);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When finding user with each category Then should return user with correct category")
    void whenFindingUserWithEachCategory_thenShouldReturnUserWithCorrectCategory() {
        // Given
        String document = "00588380903";

        for (Category category : Category.values()) {
            expectedUser.setCategory(category);
            when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

            // When
            User result = findUserByDocument.execute(document, transactionId);

            // Then
            assertNotNull(result);
            assertEquals(category, result.getCategory());
        }

        verify(userRepository, times(Category.values().length)).findByDocument(document);
    }

    @Test
    @DisplayName("When finding user with special characters in name Then should return user correctly")
    void whenFindingUserWithSpecialCharactersInName_thenShouldReturnUserCorrectly() {
        // Given
        String document = "00588380903";
        expectedUser.setFullName("José María Ñoño de Souza");
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        User result = findUserByDocument.execute(document, transactionId);

        // Then
        assertEquals("José María Ñoño de Souza", result.getFullName());

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When repository returns empty optional Then should throw UserNotFoundException with correct code")
    void whenRepositoryReturnsEmptyOptional_thenShouldThrowUserNotFoundExceptionWithCorrectCode() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            findUserByDocument.execute(document, transactionId);
        });

        assertNotNull(exception.getCode());
        assertFalse(exception.getCode().isEmpty());

        verify(userRepository, times(1)).findByDocument(document);
    }

    @Test
    @DisplayName("When finding user Then should call repository with correct document")
    void whenFindingUser_thenShouldCallRepositoryWithCorrectDocument() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        findUserByDocument.execute(document, transactionId);

        // Then
        verify(userRepository, times(1)).findByDocument(eq(document));
    }

    @Test
    @DisplayName("When finding user multiple times Then should call repository each time")
    void whenFindingUserMultipleTimes_thenShouldCallRepositoryEachTime() {
        // Given
        String document = "00588380903";
        when(userRepository.findByDocument(document)).thenReturn(Optional.of(expectedUser));

        // When
        findUserByDocument.execute(document, transactionId);
        findUserByDocument.execute(document, transactionId);
        findUserByDocument.execute(document, transactionId);

        // Then
        verify(userRepository, times(3)).findByDocument(document);
    }
}