package com.atuantes.mentes.user.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}
