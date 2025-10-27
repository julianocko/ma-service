package com.atuantes.mentes.user.domain.exception.handler;

import com.atuantes.mentes.user.domain.exception.UserException;
import com.atuantes.mentes.user.domain.exception.model.ExceptionResponse;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ControllerAdvice
public class CustomEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .code("VAL-0001")
                .message(UserMessage.ERROR_OCCURS_WHILE_PROCESSING_REQUEST.getMessage())
                .details(request.getDescription(false))
                .errors(errors)
                .build();

        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(UserMessage.ERROR_OCCURS_WHILE_PROCESSING_REQUEST.getMessage())
                .details(request.getDescription(false))
                .errors(List.of(ex.getMessage()))
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public final ResponseEntity<ExceptionResponse> handleUserException(UserException ex, WebRequest request) {
        log.error(LogMessage.LOG_ERROR.getMessage(), ex.getClass().getName(), ex.getCode(), ex.getMessage());
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getCode())
                .message(UserMessage.ERROR_OCCURS_WHILE_PROCESSING_REQUEST.getMessage())
                .details(request.getDescription(false))
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatusCode.valueOf(ex.getStatus().value()));
    }
}
