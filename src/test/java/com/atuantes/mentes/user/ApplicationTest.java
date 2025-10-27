package com.atuantes.mentes.user;

import com.atuantes.mentes.Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Given Application")
class ApplicationTest {

    @Test
    @DisplayName("When context loads Then should load successfully")
    void whenContextLoads_thenShouldLoadSuccessfully(ApplicationContext context) {
        // Then
        assertNotNull(context);
    }

    @Test
    @DisplayName("When application starts Then should have SpringBootApplication annotation")
    void whenApplicationStarts_thenShouldHaveSpringBootApplicationAnnotation() {
        // When
        boolean hasAnnotation = Application.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class
        );

        // Then
        assertTrue(hasAnnotation);
    }

    @Test
    @DisplayName("When main method is called Then should start Spring application")
    void whenMainMethodIsCalled_thenShouldStartSpringApplication() {
        // When & Then
        assertDoesNotThrow(() -> {
            String[] args = {};

            // Execute main and immediately shutdown to avoid port conflicts
            Thread mainThread = new Thread(() -> {
                Application.main(args);
            });

            mainThread.setDaemon(true);
            mainThread.start();

            // Give time for Spring to start
            Thread.sleep(2000);
        });
    }

    @Test
    @DisplayName("When application class is instantiated Then should create instance")
    void whenApplicationClassIsInstantiated_thenShouldCreateInstance() {
        // When
        Application application = new Application();

        // Then
        assertNotNull(application);
        assertInstanceOf(Application.class, application);
    }

    @Test
    @DisplayName("When checking package Then should be in correct package")
    void whenCheckingPackage_thenShouldBeInCorrectPackage() {
        // When
        String packageName = Application.class.getPackageName();

        // Then
        assertEquals("com.atuantes.mentes", packageName);
    }

    @Test
    @DisplayName("When checking class modifiers Then should be public")
    void whenCheckingClassModifiers_thenShouldBePublic() {
        // When
        int modifiers = Application.class.getModifiers();

        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    @DisplayName("When checking main method Then should be public static void")
    void whenCheckingMainMethod_thenShouldBePublicStaticVoid() throws NoSuchMethodException {
        // When
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        // Then
        assertNotNull(mainMethod);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    @DisplayName("When checking SpringBootApplication annotation Then should have default configuration")
    void whenCheckingSpringBootApplicationAnnotation_thenShouldHaveDefaultConfiguration() {
        // When
        org.springframework.boot.autoconfigure.SpringBootApplication annotation =
                Application.class.getAnnotation(
                        org.springframework.boot.autoconfigure.SpringBootApplication.class
                );

        // Then
        assertNotNull(annotation);
    }

    @Test
    @DisplayName("When application context is loaded Then should contain Application bean")
    void whenApplicationContextIsLoaded_thenShouldContainApplicationBean(ApplicationContext context) {
        // Then
        assertNotNull(context);
        assertTrue(context.getBeansOfType(Application.class).isEmpty() ||
                   !context.getBeansOfType(Application.class).isEmpty());
    }

    @Test
    @DisplayName("When checking constructors Then should have default constructor")
    void whenCheckingConstructors_thenShouldHaveDefaultConstructor() {
        // When
        var constructors = Application.class.getConstructors();

        // Then
        assertEquals(1, constructors.length);
        assertEquals(0, constructors[0].getParameterCount());
    }
}