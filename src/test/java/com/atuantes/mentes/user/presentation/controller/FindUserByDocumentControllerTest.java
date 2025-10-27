package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.query.FindUserByDocumentQuery;
import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.presentation.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindUserByDocumentControllerTest {

    @Mock
    private FindUserByDocumentUseCase findUserByDocumentUseCase;

    @InjectMocks
    private FindUserByDocumentController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindUserByDocumentAndReturn200() {
        // Given
        String documentWithMask = "123.456.789-00";
        String normalizedDocument = "12345678900";

        UserResponseDto expectedResponse = new UserResponseDto(
                UUID.randomUUID(),
                "Test User",
                true,
                normalizedDocument,
                "test@example.com",
                "1234567890",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(findUserByDocumentUseCase.execute(any(FindUserByDocumentQuery.class)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<UserResponseDto> response = controller.findByDocument(documentWithMask);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        // Verify that the document was normalized before calling the use case
        ArgumentCaptor<FindUserByDocumentQuery> queryCaptor = ArgumentCaptor.forClass(FindUserByDocumentQuery.class);
        verify(findUserByDocumentUseCase).execute(queryCaptor.capture());
        assertEquals(normalizedDocument, queryCaptor.getValue().document());
    }

    @Test
    void shouldNormalizeDocumentByRemovingNonDigits() {
        // Given
        String documentWithSpecialChars = "123.456.789-00";
        String expectedNormalizedDocument = "12345678900";

        UserResponseDto expectedResponse = new UserResponseDto(
                UUID.randomUUID(),
                "Test User",
                true,
                expectedNormalizedDocument,
                "test@example.com",
                "1234567890",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(findUserByDocumentUseCase.execute(any(FindUserByDocumentQuery.class)))
                .thenReturn(expectedResponse);

        // When
        controller.findByDocument(documentWithSpecialChars);

        // Then
        ArgumentCaptor<FindUserByDocumentQuery> queryCaptor = ArgumentCaptor.forClass(FindUserByDocumentQuery.class);
        verify(findUserByDocumentUseCase).execute(queryCaptor.capture());
        assertEquals(expectedNormalizedDocument, queryCaptor.getValue().document());
    }
}
