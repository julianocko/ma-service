package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserPersistenceException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.domain.service.UserInsertRepository;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor
public class UserInsertRepostImpl implements UserInsertRepository {

    private final UserRepository userRepository;

    @Override
    public User insert(User user, UUID transactionId) {
        log.info(LogMessage.LOG_START_REPOSITORY.getMessage(), "insert user", transactionId);
        try {
            var persistedUser = userRepository.save(user.getFullName(),
                    user.getDocument(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getBirthdate(),
                    user.getCategory().name());
            log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "insert user", transactionId);
            return persistedUser;
        } catch (DuplicateKeyException e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getName(),
                    UserErrorMessage.USER_INSERT_ERROR.getCode(), e.getMessage(), transactionId);
            throw new UserPersistenceException(UserErrorMessage.DUPLICATE_DOCUMENT_ERROR.getCode(),
                    UserErrorMessage.DUPLICATE_DOCUMENT_ERROR.getMessage());
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getName(),
                    UserErrorMessage.USER_INSERT_ERROR.getCode(), e.getMessage(), transactionId);
            throw new UserPersistenceException(UserErrorMessage.USER_INSERT_ERROR.getCode(), e.getMessage());
        }
    }
}
