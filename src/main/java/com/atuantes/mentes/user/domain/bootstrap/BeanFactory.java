package com.atuantes.mentes.user.domain.bootstrap;

import com.atuantes.mentes.user.application.usecase.CreateUserUseCase;
import com.atuantes.mentes.user.domain.service.UserInsertRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanFactory {

    @Bean
    public CreateUserUseCase createUserUseCase(UserInsertRepository userInsertRepository) {
        return new CreateUserUseCase(userInsertRepository);
    }

}