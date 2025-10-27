package com.atuantes.mentes.user.presentation.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Given ValidAgeValidator")
class ValidAgeValidatorTest {

    private ValidAgeValidator validator;

    @Mock
    private ValidAge validAge;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ValidAgeValidator();
    }

    @Test
    @DisplayName("When birthdate is null Then should return true")
    void whenBirthdateIsNull_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);

        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When birthdate is in the future Then should return false")
    void whenBirthdateIsInFuture_thenShouldReturnFalse() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate futureBirthdate = LocalDate.now().plusDays(1);

        // When
        boolean result = validator.isValid(futureBirthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When birthdate is today Then should return true")
    void whenBirthdateIsToday_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate today = LocalDate.now();

        // When
        boolean result = validator.isValid(today, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When age is exactly minimum Then should return true")
    void whenAgeIsExactlyMinimum_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(18);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(18);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When age is exactly maximum Then should return true")
    void whenAgeIsExactlyMaximum_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(100);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(100);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When age is below minimum Then should return false")
    void whenAgeIsBelowMinimum_thenShouldReturnFalse() {
        // Given
        when(validAge.min()).thenReturn(18);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(17);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When age is above maximum Then should return false")
    void whenAgeIsAboveMaximum_thenShouldReturnFalse() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(100);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(101);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When age is within range Then should return true")
    void whenAgeIsWithinRange_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(18);
        when(validAge.max()).thenReturn(65);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(30);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 17, 18, 25, 50, 99, 100, 150})
    @DisplayName("When validating various ages within default range Then should return true")
    void whenValidatingVariousAgesWithinDefaultRange_thenShouldReturnTrue(int years) {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(years);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When min is 0 and max is 150 Then should accept newborn")
    void whenMinIs0AndMaxIs150_thenShouldAcceptNewborn() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now();

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When min is 0 and max is 150 Then should accept very old person")
    void whenMinIs0AndMaxIs150_thenShouldAcceptVeryOldPerson() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(120);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When birthdate is one day in future Then should return false")
    void whenBirthdateIsOneDayInFuture_thenShouldReturnFalse() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().plusDays(1);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When birthdate is one year in future Then should return false")
    void whenBirthdateIsOneYearInFuture_thenShouldReturnFalse() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().plusYears(1);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When birthdate is yesterday Then should return true")
    void whenBirthdateIsYesterday_thenShouldReturnTrue() {
        // Given
        when(validAge.min()).thenReturn(0);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusDays(1);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("When age is exactly 17 years 364 days Then should return false for min 18")
    void whenAgeIsAlmost18_thenShouldReturnFalseForMin18() {
        // Given
        when(validAge.min()).thenReturn(18);
        when(validAge.max()).thenReturn(150);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(18).plusDays(1);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When initializing with custom min and max Then should use those values")
    void whenInitializingWithCustomMinAndMax_thenShouldUseThoseValues() {
        // Given
        when(validAge.min()).thenReturn(21);
        when(validAge.max()).thenReturn(60);
        validator.initialize(validAge);
        LocalDate birthdate = LocalDate.now().minusYears(20);

        // When
        boolean result = validator.isValid(birthdate, context);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("When birthdate makes age exactly on boundary Then should validate correctly")
    void whenBirthdateMakesAgeExactlyOnBoundary_thenShouldValidateCorrectly() {
        // Given
        when(validAge.min()).thenReturn(18);
        when(validAge.max()).thenReturn(65);
        validator.initialize(validAge);
        LocalDate birthdate18 = LocalDate.now().minusYears(18);
        LocalDate birthdate65 = LocalDate.now().minusYears(65);

        // When
        boolean result18 = validator.isValid(birthdate18, context);
        boolean result65 = validator.isValid(birthdate65, context);

        // Then
        assertTrue(result18);
        assertTrue(result65);
    }
}