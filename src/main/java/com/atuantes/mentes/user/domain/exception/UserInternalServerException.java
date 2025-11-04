package com.atuantes.mentes.user.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserInternalServerException extends UserException {
    public UserInternalServerException(String code, String message) {
        super(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
