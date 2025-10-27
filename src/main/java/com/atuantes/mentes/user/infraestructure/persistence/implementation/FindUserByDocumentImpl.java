package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FindUserByDocumentImpl implements FindUserByDocument {

    private final UserRepository userRepository;

    public User execute(String document, UUID transactionId) {
        log.info(LogMessage.LOG_START_REPOSITORY.getMessage(), "find user by document", transactionId);
        User user = userRepository.findByDocument(document)
                .orElseThrow(() -> new UserNotFoundException(
                        UserErrorMessage.USER_NOT_FOUND.getCode(),
                        UserErrorMessage.USER_NOT_FOUND.getMessage()
                ));
        log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "find user by document", transactionId);
        return user;
    }
}
