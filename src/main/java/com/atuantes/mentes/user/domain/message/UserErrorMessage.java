package com.atuantes.mentes.user.domain.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserErrorMessage {

    CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR("USER-0001",
            UserMessage.CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR.getMessage()),

    CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR("USER-0002",
            UserMessage.CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR.getMessage()),

    INVALID_DOCUMENT("USER-0003", UserMessage.INVALID_DOCUMENT.getMessage()),

    INVALID_CPF("USER-0004", UserMessage.INVALID_CPF.getMessage()),

    USER_INSERT_ERROR("USER-0005", UserMessage.USER_INSERT_ERROR.getMessage()),

    DUPLICATE_DOCUMENT_ERROR("USER-0006", UserMessage.DUPLICATE_DOCUMENT_ERROR.getMessage()),

    USER_NOT_FOUND("USER-0007", "Usuário não encontrado para o documento informado.")
    ;

    private final String code;
    private final String message;

}
