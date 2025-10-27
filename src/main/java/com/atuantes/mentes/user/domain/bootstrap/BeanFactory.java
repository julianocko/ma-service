package com.atuantes.mentes.user.domain.bootstrap;

import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.domain.mapper.CreateUserCommandToUser;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import com.atuantes.mentes.user.domain.service.UserInsert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanFactory {

    @Bean
    public CreateUserUseCase createUserUseCase(CreateUserCommandToUser createUserCommandToUser,
                                               UserInsert userInsert) {
        return new CreateUserUseCase(createUserCommandToUser, userInsert);
    }

    @Bean
    public FindUserByDocumentUseCase findUserByDocumentUseCase(FindUserByDocument findUserByDocument) {
        return new FindUserByDocumentUseCase(findUserByDocument);
    }

}