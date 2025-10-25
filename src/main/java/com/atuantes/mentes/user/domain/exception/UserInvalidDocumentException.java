package com.atuantes.mentes.user.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserInvalidDocumentException extends UserException {
    public UserInvalidDocumentException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
