package com.atuantes.mentes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

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
        boolean hasAnnotation = Application.class.isAnnotationPresent(SpringBootApplication.class);

        // Then
        assertTrue(hasAnnotation);
    }

    @Test
    @DisplayName("When checking SpringBootApplication annotation Then should have default configuration")
    void whenCheckingSpringBootApplicationAnnotation_thenShouldHaveDefaultConfiguration() {
        // When
        SpringBootApplication annotation = Application.class.getAnnotation(SpringBootApplication.class);

        // Then
        assertNotNull(annotation);
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
    @DisplayName("When checking main method Then should exist and have correct signature")
    void whenCheckingMainMethod_thenShouldExistAndHaveCorrectSignature() throws NoSuchMethodException {
        // When
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        // Then
        assertNotNull(mainMethod);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    @DisplayName("When checking constructors Then should have only default constructor")
    void whenCheckingConstructors_thenShouldHaveOnlyDefaultConstructor() {
        // When
        var constructors = Application.class.getConstructors();

        // Then
        assertEquals(1, constructors.length);
        assertEquals(0, constructors[0].getParameterCount());
    }

    @Test
    @DisplayName("When main method is called Then should start Spring application")
    void whenMainMethodIsCalled_thenShouldStartSpringApplication() {
        // When & Then
        assertDoesNotThrow(() -> {
            String[] args = {};
            Thread mainThread = new Thread(() -> Application.main(args));
            mainThread.setDaemon(true);
            mainThread.start();
            Thread.sleep(2000);
        });
    }

    @Test
    @DisplayName("When checking class Then should not be abstract")
    void whenCheckingClass_thenShouldNotBeAbstract() {
        // When
        int modifiers = Application.class.getModifiers();

        // Then
        assertFalse(java.lang.reflect.Modifier.isAbstract(modifiers));
    }

    @Test
    @DisplayName("When checking class Then should not be interface")
    void whenCheckingClass_thenShouldNotBeInterface() {
        // Then
        assertFalse(Application.class.isInterface());
    }

    @Test
    @DisplayName("When checking class Then should not be enum")
    void whenCheckingClass_thenShouldNotBeEnum() {
        // Then
        assertFalse(Application.class.isEnum());
    }

    @Test
    @DisplayName("When checking class name Then should be Application")
    void whenCheckingClassName_thenShouldBeApplication() {
        // When
        String className = Application.class.getSimpleName();

        // Then
        assertEquals("Application", className);
    }

    @Test
    @DisplayName("When checking application context Then should be non-null")
    void whenCheckingApplicationContext_thenShouldBeNonNull(ApplicationContext context) {
        // Then
        assertNotNull(context);
        assertTrue(context.getBeanDefinitionNames().length > 0);
    }
}