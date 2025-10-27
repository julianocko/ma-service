package com.atuantes.mentes.user.presentation.dto;

import java.time.OffsetDateTime;

public record ErrorResponseDto(
        String code,
        String message,
        OffsetDateTime timestamp
) {
}
