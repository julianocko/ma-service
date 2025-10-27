package com.atuantes.mentes.user.presentation.controller;

import com.atuantes.mentes.user.application.query.FindUserByDocumentQuery;
import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.presentation.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FindUserByDocumentController {

    private final FindUserByDocumentUseCase findUserByDocumentUseCase;

    @GetMapping(value = "/document/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> findByDocument(@PathVariable String document) {
        String normalizedDocument = document.replaceAll("\\D", "");
        FindUserByDocumentQuery query = new FindUserByDocumentQuery(normalizedDocument);
        UserResponseDto response = findUserByDocumentUseCase.execute(query);
        return ResponseEntity.ok(response);
    }
}
