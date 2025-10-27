package com.atuantes.mentes.user.application.usecase;

import com.atuantes.mentes.user.application.query.FindUserByDocumentQuery;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserNotFoundException;
import com.atuantes.mentes.user.domain.mapper.UserToUserResponseDto;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import com.atuantes.mentes.user.infraestructure.persistence.repository.UserRepository;
import com.atuantes.mentes.user.presentation.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByDocumentUseCase {

    private final UserRepository userRepository;
    private final UserToUserResponseDto mapper;

    public UserResponseDto execute(FindUserByDocumentQuery query) {
        User user = userRepository.findByDocument(query.document())
                .orElseThrow(() -> new UserNotFoundException(
                        UserErrorMessage.USER_NOT_FOUND.getCode(),
                        UserErrorMessage.USER_NOT_FOUND.getMessage()
                ));

        return mapper.map(user);
    }
}
