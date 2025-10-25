package com.atuantes.mentes.user.domain.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserMessage {

    CREATE_USER_DTO_TO_COMMAND_MAPPER_ERROR("Ocorreu um erro no mapeamento do CreateUserDTO para CreateUserCommand."),
    CREATE_USER_COMMAND_TO_USER_MAPPER_ERROR("Ocorreu um erro no mapeamento do CreateUserCommand para User."),
    INVALID_DOCUMENT("O documento fornecido é inválido."),
    INVALID_CPF("O documento fornecido não representa um CPF válido."),
    USER_INSERT_ERROR("Ocorreu um erro ao inserir o usuário no repositório."),
    DUPLICATE_DOCUMENT_ERROR("Já existe um usuário cadastrado com o mesmo documento.");

    private final String message;

}
