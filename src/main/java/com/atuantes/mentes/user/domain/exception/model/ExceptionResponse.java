package com.atuantes.mentes.user.domain.exception.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ExceptionResponse(
        LocalDateTime timestamp,
        String code,
        String message,
        String details,
        List<String> errors) {
}
