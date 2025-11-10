package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.presentation.dto.UserDto;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        UserDto expectedDtoUser = new UserDto();
        expectedDtoUser.setId(UUID.randomUUID());
        expectedDtoUser.setFullName("João Silva");
        expectedDtoUser.setActive(true);
        expectedDtoUser.setDocument("00588380903");
        expectedDtoUser.setEmail("joao@test.com");
        expectedDtoUser.setPhone("11999999999");
        expectedDtoUser.setBirthdate(LocalDate.of(1990, 1, 1));
        expectedDtoUser.setCategory(Category.FATHER);
        expectedDtoUser.add(linkTo(methodOn(FindUserByDocumentController.class).findByDocument(transactionId, "00588380903"))
                .withSelfRel().withType("GET"));
        expectedDtoUser.add(linkTo(methodOn(DeleteUserByDocumentController.class).deleteByDocument(transactionId, "00588380903"))
                .withRel("delete").withType("DELETE"));
        expectedDtoUser.add(linkTo(methodOn(CreateUserController.class).createUser(transactionId, null))
                .withRel("create").withType("POST"));
        expectedDtoUser.add(linkTo(methodOn(UpdateUserByDocumentController.class).updateUserByDocument(transactionId,
                expectedDtoUser.getDocument(), null)).withRel("update").withType("PUT"));
    }

    @Test
    @DisplayName("When finding user by valid document Then should return user with status 200")
    void whenFindingUserByValidDocument_thenShouldReturnUserWithStatus200() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

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
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, documentWithMask);

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
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> controller.findByDocument(transactionId, document));

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
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.findByDocument(transactionId, document));

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
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
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
        ResponseEntity<UserDto> response1 = controller.findByDocument(transactionId, document1);
        ResponseEntity<UserDto> response2 = controller.findByDocument(transactionId, document2);

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

    @Test
    @DisplayName("When finding user Then should include self link")
    void whenFindingUser_thenShouldIncludeSelfLink() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertTrue(response.getBody().hasLink("self"));
        assertEquals("GET", response.getBody().getLink("self").get().getType());
    }

    @Test
    @DisplayName("When finding user Then should include delete link")
    void whenFindingUser_thenShouldIncludeDeleteLink() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertTrue(response.getBody().hasLink("delete"));
        assertEquals("DELETE", response.getBody().getLink("delete").get().getType());
    }

    @Test
    @DisplayName("When finding user Then should include create link")
    void whenFindingUser_thenShouldIncludeCreateLink() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertTrue(response.getBody().hasLink("create"));
        assertEquals("POST", response.getBody().getLink("create").get().getType());
    }

    @Test
    @DisplayName("When finding user Then should include update link")
    void whenFindingUser_thenShouldIncludeUpdateLink() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertTrue(response.getBody().hasLink("update"));
        assertEquals("PUT", response.getBody().getLink("update").get().getType());
    }

    @Test
    @DisplayName("When finding user Then should include all four HATEOAS links")
    void whenFindingUser_thenShouldIncludeAllFourHateoasLinks() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getLinks().toList().size());
        assertTrue(response.getBody().hasLink("self"));
        assertTrue(response.getBody().hasLink("delete"));
        assertTrue(response.getBody().hasLink("create"));
        assertTrue(response.getBody().hasLink("update"));
    }

    @Test
    @DisplayName("When finding user Then self link should point to correct endpoint")
    void whenFindingUser_thenSelfLinkShouldPointToCorrectEndpoint() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        String selfLink = response.getBody().getLink("self").get().getHref();
        assertTrue(selfLink.contains("/users/document/" + document));
    }

    @Test
    @DisplayName("When finding user Then delete link should point to correct endpoint")
    void whenFindingUser_thenDeleteLinkShouldPointToCorrectEndpoint() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        String deleteLink = response.getBody().getLink("delete").get().getHref();
        assertTrue(deleteLink.contains("/users/document/" + document));
    }

    @Test
    @DisplayName("When finding user Then create link should point to correct endpoint")
    void whenFindingUser_thenCreateLinkShouldPointToCorrectEndpoint() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        String createLink = response.getBody().getLink("create").get().getHref();
        assertTrue(createLink.contains("/users"));
    }

    @Test
    @DisplayName("When finding user Then update link should point to correct endpoint with document")
    void whenFindingUser_thenUpdateLinkShouldPointToCorrectEndpointWithDocument() {
        // Given
        String document = "00588380903";
        when(findUserByDocumentUseCase.findUserByDocument(document, transactionId))
                .thenReturn(expectedUser);

        // When
        ResponseEntity<UserDto> response = controller.findByDocument(transactionId, document);

        // Then
        assertNotNull(response.getBody());
        String updateLink = response.getBody().getLink("update").get().getHref();
        assertTrue(updateLink.contains("/users/document/" + document));
    }

    @Test
    @DisplayName("When finding user by different documents Then HATEOAS links should use correct document")
    void whenFindingUserByDifferentDocuments_thenHateoasLinksShouldUseCorrectDocument() {
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
        ResponseEntity<UserDto> response1 = controller.findByDocument(transactionId, document1);
        ResponseEntity<UserDto> response2 = controller.findByDocument(transactionId, document2);

        // Then
        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());

        String selfLink1 = response1.getBody().getLink("self").get().getHref();
        String selfLink2 = response2.getBody().getLink("self").get().getHref();

        assertTrue(selfLink1.contains(document1));
        assertTrue(selfLink2.contains(document2));
        assertNotEquals(selfLink1, selfLink2);
    }
}