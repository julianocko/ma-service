package com.atuantes.mentes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTest {

    @Test
    @DisplayName("When application context loads Then should not be null")
    void whenApplicationContextLoads_thenShouldNotBeNull() {
        // When & Then
        assertDoesNotThrow(() -> SpringApplication.run(Application.class));
    }


    @Test
    @DisplayName("When application starts Then should have SpringBootApplication annotation")
    void whenApplicationStarts_thenShouldHaveSpringBootApplicationAnnotation() {
        // When
        SpringBootApplication annotation = Application.class.getAnnotation(SpringBootApplication.class);

        // Then
        assertNotNull(annotation);
    }

    @Test
    @DisplayName("When application class is loaded Then should not be null")
    void whenApplicationClassIsLoaded_thenShouldNotBeNull() {
        // When & Then
        assertNotNull(Application.class);
    }

    @Test
    @DisplayName("When application starts Then should be able to create instance")
    void whenApplicationStarts_thenShouldBeAbleToCreateInstance() {
        // When & Then
        assertDoesNotThrow(Application::new);
    }

   @Test
   @DisplayName("When application starts Then should scan base packages correctly")
   void whenApplicationStarts_thenShouldScanBasePackagesCorrectly() {
       // When
       SpringBootApplication annotation = Application.class.getAnnotation(SpringBootApplication.class);

       // Then
       assertNotNull(annotation);
       String[] scanBasePackages = annotation.scanBasePackages();
       assertNotNull(scanBasePackages);
       assertEquals(0, scanBasePackages.length, "scanBasePackages should be empty when not explicitly configured");
   }
}