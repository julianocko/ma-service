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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
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
            );
            log.info(LogMessage.LOG_END_REPOSITORY.getMessage(), "update user by document", transactionId);
            
            if (updatedUser.isEmpty()) {
                log.error(LogMessage.LOG_ERROR.getMessage(), "UserNotFoundException",
                        "USER-404", String.format("User not found for document %s", user.getDocument()), transactionId);
                throw new UserNotFoundException("USER-404", 
                        String.format("User not found for document %s", user.getDocument()));
            }
            
            log.info(LogMessage.LOG_END_SERVICE.getMessage(), "update user", transactionId);
            return updatedUser.get();
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(LogMessage.LOG_ERROR.getMessage(), e.getClass().getName(),
                    UserErrorMessage.USER_INSERT_ERROR.getCode(), e.getMessage(), transactionId);
            throw new UserPersistenceException("USER-UPDATE-ERROR", "Erro ao atualizar usuário");
        }
    }
}
