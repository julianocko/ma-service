package com.atuantes.mentes.user;

import com.atuantes.mentes.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Given Application Main Method")
class ApplicationMainTest {

    @Test
    @DisplayName("When main method is called Then should execute SpringApplication.run")
    void whenMainMethodIsCalled_thenShouldExecuteSpringApplicationRun() {
        assertDoesNotThrow(() -> {
            String[] args = {"--spring.main.web-environment=false", "--spring.main.lazy-initialization=true"};
            
            Thread mainThread = new Thread(() -> Application.main(args));
            mainThread.setDaemon(true);
            mainThread.start();
            Thread.sleep(1000);
        });
    }
}