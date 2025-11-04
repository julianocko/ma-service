package com.atuantes.mentes.user.domain.bootstrap;

import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.application.usecase.DeleteUserByDocumentUseCase;
import com.atuantes.mentes.user.application.usecase.FindUserByDocumentUseCase;
import com.atuantes.mentes.user.application.usecase.UpdateUserUseCase;
import com.atuantes.mentes.user.domain.mapper.CreateUserCommandToUser;
import com.atuantes.mentes.user.domain.mapper.UpdateUserCommandToUser;
import com.atuantes.mentes.user.domain.service.FindUserByDocument;
import com.atuantes.mentes.user.domain.service.UserDelete;
import com.atuantes.mentes.user.domain.service.UserInsert;
import com.atuantes.mentes.user.domain.service.UserUpdate;
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

    @Bean
    public UpdateUserUseCase updateUserUseCase(UpdateUserCommandToUser updateUserCommandToUser,
                                               UserUpdate userUpdate) {
        return new UpdateUserUseCase(updateUserCommandToUser, userUpdate);
    }

    @Bean
    public UpdateUserCommandToUser updateUserCommandToUser() {
        return new UpdateUserCommandToUser();
    }

    @Bean
    public DeleteUserByDocumentUseCase deleteUserByDocumentUseCase(UserDelete userDelete) {
        return new DeleteUserByDocumentUseCase(userDelete);
    }

}