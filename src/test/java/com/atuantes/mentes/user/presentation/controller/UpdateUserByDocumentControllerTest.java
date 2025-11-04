package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.command.UpdateUserCommand;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
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
    private String document;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();
        document = "00588380903";

        validDto = new UpdateUserDto(
                "João Silva Updated",
                "joao.updated@test.com",
                "11988888888",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                true
        );

        validCommand = new UpdateUserCommand();
        validCommand.setDocument(document);
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
        updatedUser.setDocument(document);
        updatedUser.setEmail("joao.updated@test.com");
        updatedUser.setPhone("11988888888");
        updatedUser.setBirthdate(LocalDate.of(1990, 1, 1));
        updatedUser.setCategory(Category.FATHER);
    }

    @Test
    @DisplayName("When updating user with valid data Then should return 200 OK")
    void whenUpdatingUserWithValidData_thenShouldReturn200Ok() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedUser.getId().toString()))
                .andExpect(jsonPath("$.fullName").value("João Silva Updated"))
                .andExpect(jsonPath("$.document").value(document))
                .andExpect(jsonPath("$.email").value("joao.updated@test.com"))
                .andExpect(jsonPath("$.phone").value("11988888888"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.category").value("FATHER"));

        verify(updateUserDtoToCommand, times(1)).toCommand(eq(document), any(UpdateUserDto.class));
        verify(updateUserUseCase, times(1)).updateUser(any(UpdateUserCommand.class), eq(transactionId));
    }

    @Test
    @DisplayName("When updating user with document containing special characters Then should normalize document")
    void whenUpdatingUserWithDocumentContainingSpecialCharacters_thenShouldNormalizeDocument() throws Exception {
        // Given
        String documentWithMask = "005.883.809-03";
        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/users/document/" + documentWithMask)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());

        verify(updateUserDtoToCommand, times(1)).toCommand(eq(document), any(UpdateUserDto.class));
    }

    @Test
    @DisplayName("When updating non-existent user Then should return 404 NOT FOUND")
    void whenUpdatingNonExistentUser_thenShouldReturn404NotFound() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class)))
                .thenThrow(new UserNotFoundException("USER-404", "User not found for document " + document));

        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isNotFound());

        verify(updateUserUseCase, times(1)).updateUser(any(UpdateUserCommand.class), eq(transactionId));
    }

    @Test
    @DisplayName("When updating user without transaction id Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithoutTransactionId_thenShouldReturn400BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
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
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
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
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When updating user with null active Then should return 400 BAD REQUEST")
    void whenUpdatingUserWithNullActive_thenShouldReturn400BadRequest() throws Exception {
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
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When updating user with active=false Then should update successfully")
    void whenUpdatingUserWithActiveFalse_thenShouldUpdateSuccessfully() throws Exception {
        // Given
        UpdateUserDto dtoInactive = new UpdateUserDto(
                "João Silva",
                "joao@test.com",
                "11999999999",
                LocalDate.of(1990, 1, 1),
                Category.FATHER,
                false
        );

        validCommand.setActive(false);
        updatedUser.setActive(false);

        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInactive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @DisplayName("When mapper throws exception Then should propagate error")
    void whenMapperThrowsException_thenShouldPropagateError() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class)))
                .thenThrow(new UserIllegalArgumentException("UPDATE-001", "Mapping error"));

        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().is4xxClientError());

        verify(updateUserUseCase, never()).updateUser(any(), any());
    }

    @Test
    @DisplayName("When use case throws exception Then should propagate error")
    void whenUseCaseThrowsException_thenShouldPropagateError() throws Exception {
        // Given
        when(updateUserDtoToCommand.toCommand(eq(document), any(UpdateUserDto.class))).thenReturn(validCommand);
        when(updateUserUseCase.updateUser(any(UpdateUserCommand.class), any(UUID.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().is5xxServerError());
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
        mockMvc.perform(put("/users/document/" + document)
                        .header("x-transaction-id", transactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
