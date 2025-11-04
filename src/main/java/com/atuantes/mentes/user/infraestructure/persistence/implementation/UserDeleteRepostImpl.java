package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.domain.service.UserDelete;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDeleteRepostImpl implements UserDelete {

    private final UserRepository userRepository;

    @Override
    public void deleteByDocument(String document, UUID transactionId) {
        log.info(LogMessage.LOG_START_SERVICE.getMessage(), "delete user", transactionId);

        // Check if user exists before deleting
        log.info(LogMessage.LOG_START_REPOSITORY.getMessage(), "find user by document", transactionId);
        userRepository.findByDocument(document)
                .orElseThrow(() -> new UserNotFoundException(
                        UserErrorMessage.USER_NOT_FOUND.getCode(),
                        UserErrorMessage.USER_NOT_FOUND.getMessage()
                ));
        log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "find user by document", transactionId);

        // Delete the user
        log.info(LogMessage.LOG_START_REPOSITORY.getMessage(), "delete user by document", transactionId);
        userRepository.deleteByDocument(document);
        log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "delete user by document", transactionId);

        log.info(LogMessage.LOG_END_SERVICE.getMessage(), "delete user", transactionId);
    }
}
