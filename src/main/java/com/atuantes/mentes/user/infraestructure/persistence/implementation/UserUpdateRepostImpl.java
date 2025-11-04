package com.atuantes.mentes.user.infraestructure.persistence.implementation;

import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.exception.UserPersistenceException;
import com.atuantes.mentes.user.domain.message.LogMessage;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.domain.service.UserUpdate;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor
public class UserUpdateRepostImpl implements UserUpdate {

    private final UserRepository userRepository;

    @Override
    public User update(User user, UUID transactionId) {
        log.info(LogMessage.LOG_START_SERVICE.getMessage(), "update user", transactionId);
        try {
            log.info(LogMessage.LOG_START_REPOSITORY.getMessage(), "update user by document", transactionId);
            
            var updatedUser = userRepository.updateByDocument(
                    user.getDocument(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getBirthdate(),
                    user.getCategory().name(),
                    user.isActive()
            ).orElseThrow(() -> {
                log.error(LogMessage.LOG_ERROR.getMessage(), UserNotFoundException.class.getSimpleName(),
                        "USER-404", "User not found for document " + user.getDocument(), transactionId);
                return new UserNotFoundException("USER-404", 
                        "User not found for document " + user.getDocument());
            });
            
            log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "update user by document", transactionId);
            log.info(LogMessage.LOG_END_SERVICE.getMessage(), "update user", transactionId);
            
            return updatedUser;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getName(),
                    "USER-UPDATE-ERROR", e.getMessage(), transactionId);
            throw new UserPersistenceException("USER-UPDATE-ERROR", 
                    "Error updating user: " + e.getMessage());
        }
    }
}
