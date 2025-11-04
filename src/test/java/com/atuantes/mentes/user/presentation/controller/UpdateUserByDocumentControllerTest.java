package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.presentation.dto.UpdateUserDto;
import com.atuantes.mentes.user.presentation.mapper.UpdateUserDtoToCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UpdateUserByDocumentController.class)
@DisplayName("Given UpdateUserByDocumentController")
class UpdateUserByDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UpdateUserDtoToCommand updateUserDtoToCommand;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    private UUID transactionId;
    private UpdateUserDto validDto;
    private UpdateUserCommand validCommand;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();

        validDto = new UpdateUserDto(
                "João Silva Updated",
                "joao.updated@test.com",
                "11988888888",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        validCommand = new UpdateUserCommand();
        validCommand.setDocument("44249385302");
        validCommand.setFullName("João Silva Updated");
        validCommand.setEmail("joao.updated@test.com");
        validCommand.setPhone("11988888888");
        validCommand.setBirthdate(LocalDate.of(1990, 1, 1));
        validCommand.setCategory(Category.FATHER);
        validCommand.setActive(true);

        updatedUser = new User();
        updatedUser.setId(UUID.randomUUID());
        updatedUser.setFullName("João Silva Updated");
        updatedUser.setActive(true);
        updatedUser.setDocument("44249385302");
        updatedUser.setEmail("joao.updated@test.com");
        updatedUser.setPhone("11988888888");
        updatedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        updatedUser.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When updating user with valid data Then should return 200 OK")
    void whenUpdatingUserWithValidData_thenShouldReturn200Ok() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq("44249385302"), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedUser.getId().toString()))
                .andExpect(jsonPath("$.fullName").value("João Silva Updated"))
                .andExpect(jsonPath("$.document").value("44249385302"))
                .andExpect(jsonPath("$.email").value("joao.updated@test.com"))
                .andExpect(jsonPath("$.phone").value("11988888888"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.category").value("FATHER"));

        verify(updateUserDtoToCommand, times(1)).toCommand(eq("44249385302"), any(UpdateUserDto.class));
        verify(updateUserUseCase, times(1)).updateUser(any(UpdateUserCommand.class), eq(transactionId));
    }

    @Test
    @DisplayName("When updating user with document containing non-digits Then should normalize document")
    void whenUpdatingUserWithDocumentContainingNonDigits_thenShouldNormalizeDocument() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq("44249385302"), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/user/document/442.493.853-02")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());

        verify(updateUserDtoToCommand, times(1)).toCommand(eq("44249385302"), any(UpdateUserDto.class));
    }

    @Test
    @DisplayName("When updating user with inactive status Then should update status")
    void whenUpdatingUserWithInactiveStatus_thenShouldUpdateStatus() throws Exception {
        // Given
        UpdateUserDto inactiveDto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                false
        );

        validCommand.setActive(false);
        updatedUser.setActive(false);

        when(updateUserDtoToCommand.toCommand(eq("44249385302"), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactiveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @DisplayName("When updating user that does not exist Then should return 404 NOT FOUND")
    void whenUpdatingUserThatDoesNotExist_thenShouldReturn404NotFound() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq("44249385302"), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class)))
                .thenThrow(new UserNotFoundException("USER-404", "User not found for document 44249385302"));

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When updating user without transaction id Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithoutTransactionId_thenShouldReturn400BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest());

        verify(updateUserDtoToCommand, never()).toCommand(any(), any());
        verify(updateUserUseCase, never()).updateUser(any(), any());
    }

    @Test
    @DisplayName("When updating user with null fullName Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithNullFullName_thenShouldReturn400BadRequest() throws Exception {
        // Given
        UpdateUserDto invalidDto = new UpdateUserDto(
                null,
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(updateUserDtoToCommand, never()).toCommand(any(), any());
        verify(updateUserUseCase, never()).updateUser(any(), any());
    }

    @Test
    @DisplayName("When updating user with invalid email Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithInvalidEmail_thenShouldReturn400BadRequest() throws Exception {
        // Given
        UpdateUserDto invalidDto = new UpdateUserDto(
                "João Silva",
                "invalid-email",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When updating user with null active status Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithNullActiveStatus_thenShouldReturn400BadRequest() throws Exception {
        // Given
        UpdateUserDto invalidDto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                null
        );

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When updating user with future birthdate Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithFutureBirthdate_thenShouldReturn400BadRequest() throws Exception {
        // Given
        UpdateUserDto invalidDto = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.now().plusDays(1),
                Category.FATHER,
                true
        );

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When response is returned Then should have correct content type")
    void whenResponseIsReturned_thenShouldHaveCorrectContentType() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq("44249385302"), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/user/document/44249385302")
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
