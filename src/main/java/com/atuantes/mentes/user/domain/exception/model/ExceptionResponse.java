package com.atuantes.mentes.user.domain.exception.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(LocalDateTime timestamp, String details, String code, String message) {
}
