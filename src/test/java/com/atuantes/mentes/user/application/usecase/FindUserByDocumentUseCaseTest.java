package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.query.FindUserByDocumentQuery;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.mapper.UserToUserResponseDto;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import com.atuantes.mentes.user.presentation.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindUserByDocumentUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserResponseDto mapper;

    @InjectMocks
    private FindUserByDocumentUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindUserByDocumentSuccessfully() {
        // Given
        String document = "12345678900";
        FindUserByDocumentQuery query = new FindUserByDocumentQuery(document);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Test User");
        user.setDocument(document);
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCategory(Category.FATHER);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserResponseDto expectedResponse = new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.isActive(),
                user.getDocument(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getCategory(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        when(userRepository.findByDocument(document)).thenReturn(Optional.of(user));
        when(mapper.map(user)).thenReturn(expectedResponse);

        // When
        UserResponseDto result = useCase.execute(query);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.fullName(), result.fullName());
        assertEquals(expectedResponse.document(), result.document());
        verify(userRepository).findByDocument(document);
        verify(mapper).map(user);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // Given
        String document = "12345678900";
        FindUserByDocumentQuery query = new FindUserByDocumentQuery(document);

        when(userRepository.findByDocument(document)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(query)
        );

        assertEquals("USER-0007", exception.getCode());
        assertEquals("Usuário não encontrado para o documento informado.", exception.getMessage());
        verify(userRepository).findByDocument(document);
        verify(mapper, never()).map(any());
    }
}
