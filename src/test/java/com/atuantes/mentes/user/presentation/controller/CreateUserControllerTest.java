package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.CreateUserCommand;
import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.presentation.dto.CreateUserDto;
import com.atuantes.mentes.user.presentation.mapper.CreateUserDtoToCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreateUserController.class)
@DisplayName("Given CreateUserController")
class CreateUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserDtoToCommand createUserDtoToCommand;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    private UUID transactionId;
    private CreateUserDto validDto;
    private CreateUserCommand validCommand;
    private User createdUser;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();

        validDto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        validCommand = new CreateUserCommand();
        validCommand.setFullName("João Silva");
        validCommand.setDocument("00588380903");
        validCommand.setEmail("joao@test.com");
        validCommand.setPhone("11999999999");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);

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
    @DisplayName("When creating user with valid data Then should return 201 CREATED")
    void whenCreatingUserWithValidData_thenShouldReturn201Created() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdUser.getId().toString()))
                .andExpect(jsonPath("$.fullName").value("João Silva"))
                .andExpect(jsonPath("$.document").value("00588380903"))
                .andExpect(jsonPath("$.email").value("joao@test.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.category").value("FATHER"));

        verify(createUserDtoToCommand, times(1)).toCommand(any(CreateUserDto.class));
        verify(createUserUseCase, times(1)).createUser(any(CreateUserCommand.class), eq(transactionId));
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("When creating user with each category Then should return 201 CREATED")
    void whenCreatingUserWithEachCategory_thenShouldReturn201Created(Category category) throws Exception {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "Test User",
                "00588380903",
                "test@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                category
        );

        validCommand.setCategory(category);
        createdUser.setCategory(category);

        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value(category.toString()));
    }

    @Test
    @DisplayName("When creating user without transaction id Then should return 400 BAD REQUEST")
    void whenCreatingUserWithoutTransactionId_thenShouldReturn400BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest());

        verify(createUserDtoToCommand, never()).toCommand(any());
        verify(createUserUseCase, never()).createUser(any(), any());
    }

    @Test
    @DisplayName("When creating user with invalid transaction id Then should return 400 BAD REQUEST")
    void whenCreatingUserWithInvalidTransactionId_thenShouldReturn400BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with null fullName Then should return 400 BAD REQUEST")
    void whenCreatingUserWithNullFullName_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                null,
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(createUserDtoToCommand, never()).toCommand(any());
        verify(createUserUseCase, never()).createUser(any(), any());
    }

    @Test
    @DisplayName("When creating user with blank fullName Then should return 400 BAD REQUEST")
    void whenCreatingUserWithBlankFullName_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                "   ",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with invalid email Then should return 400 BAD REQUEST")
    void whenCreatingUserWithInvalidEmail_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "invalid-email",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with null birthdate Then should return 400 BAD REQUEST")
    void whenCreatingUserWithNullBirthdate_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                null,
                Category.FATHER
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with future birthdate Then should return 400 BAD REQUEST")
    void whenCreatingUserWithFutureBirthdate_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.now().plusDays(1),
                Category.FATHER
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with null category Then should return 400 BAD REQUEST")
    void whenCreatingUserWithNullCategory_thenShouldReturn400BadRequest() throws Exception {
        // Given
        CreateUserDto invalidDto = new CreateUserDto(
                "João Silva",
                "00588380903",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                null
        );

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When mapper throws exception Then should propagate error")
    void whenMapperThrowsException_thenShouldPropagateError() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class)))
                .thenThrow(new UserIllegalArgumentException("USER-001", "Invalid document"));

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().is4xxClientError());

        verify(createUserUseCase, never()).createUser(any(), any());
    }

    @Test
    @DisplayName("When use case throws exception Then should propagate error")
    void whenUseCaseThrowsException_thenShouldPropagateError() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("When request has wrong content type Then should return 415 UNSUPPORTED MEDIA TYPE")
    void whenRequestHasWrongContentType_thenShouldReturn415UnsupportedMediaType() throws Exception {
        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("When request has malformed JSON Then should return 400 BAD REQUEST")
    void whenRequestHasMalformedJson_thenShouldReturn400BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When creating user with special characters in name Then should accept")
    void whenCreatingUserWithSpecialCharactersInName_thenShouldAccept() throws Exception {
        // Given
        CreateUserDto dto = new CreateUserDto(
                "José María Ñoño de Souza",
                "00588380903",
                "jose@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("When response is returned Then should have correct content type")
    void whenResponseIsReturned_thenShouldHaveCorrectContentType() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("When creating user Then should include self link")
    void whenCreatingUser_thenShouldIncludeSelfLink() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.type").value("POST"));
    }

    @Test
    @DisplayName("When creating user Then should include find link")
    void whenCreatingUser_thenShouldIncludeFindLink() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.find.href").exists())
                .andExpect(jsonPath("$._links.find.type").value("GET"));
    }

    @Test
    @DisplayName("When creating user Then should include delete link")
    void whenCreatingUser_thenShouldIncludeDeleteLink() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.delete.type").value("DELETE"));
    }

    @Test
    @DisplayName("When creating user Then should include update link")
    void whenCreatingUser_thenShouldIncludeUpdateLink() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.update.type").value("PUT"));
    }

    @Test
    @DisplayName("When creating user Then should include all four HATEOAS links")
    void whenCreatingUser_thenShouldIncludeAllFourHateoasLinks() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.find").exists())
                .andExpect(jsonPath("$._links.delete").exists())
                .andExpect(jsonPath("$._links.update").exists());
    }

    @Test
    @DisplayName("When creating user Then self link should point to users endpoint")
    void whenCreatingUser_thenSelfLinkShouldPointToUsersEndpoint() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.self.href").value(org.hamcrest.Matchers.containsString("/users")));
    }

    @Test
    @DisplayName("When creating user Then find link should contain document")
    void whenCreatingUser_thenFindLinkShouldContainDocument() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.find.href").value(org.hamcrest.Matchers.containsString("/users/document/00588380903")));
    }

    @Test
    @DisplayName("When creating user Then delete link should contain document")
    void whenCreatingUser_thenDeleteLinkShouldContainDocument() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.delete.href").value(org.hamcrest.Matchers.containsString("/users/document/00588380903")));
    }

    @Test
    @DisplayName("When creating user Then update link should contain document")
    void whenCreatingUser_thenUpdateLinkShouldContainDocument() throws Exception {
        // Given
        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.update.href").value(org.hamcrest.Matchers.containsString("/users/document/00588380903")));
    }

    @Test
    @DisplayName("When creating users with different documents Then HATEOAS links should use correct document")
    void whenCreatingUsersWithDifferentDocuments_thenHateoasLinksShouldUseCorrectDocument() throws Exception {
        // Given
        String document1 = "00588380903";
        String document2 = "98765432100";

        CreateUserDto dto1 = new CreateUserDto(
                "User 1",
                document1,
                "user1@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER
        );

        CreateUserDto dto2 = new CreateUserDto(
                "User 2",
                document2,
                "user2@test.com",
                "11988888888",
                LocalDate.of(1995, 5, 15),
                Category.MOTHER
        );

        when(createUserDtoToCommand.toCommand(any(CreateUserDto.class))).thenReturn(validCommand);
        when(createUserUseCase.createUser(any(CreateUserCommand.class), any(UUID.class))).thenReturn(createdUser);

        // When & Then - First user
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.find.href").value(org.hamcrest.Matchers.containsString(document1)));

        // When & Then - Second user
        mockMvc.perform(post("/users")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._links.find.href").value(org.hamcrest.Matchers.containsString(document2)));
    }
}