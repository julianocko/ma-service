package com.atuantes.mentes.user.domain.service;

import com.atuantes.mentes.user.domain.exception.UserInvalidDocumentException;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Given DocumentValidationService")
class DocumentValidationServiceTest {

    private DocumentValidationService service;

    @BeforeEach
    void setUp() {
        service = new DocumentValidationService();
    }

    @Test
    @DisplayName("When validating valid CPF without formatting Then should not throw exception")
    void whenValidatingValidCpfWithoutFormatting_thenShouldNotThrowException() {
        // Given
        String validCpf = "00588380903";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(validCpf));
    }

    @Test
    @DisplayName("When validating valid CPF with formatting Then should not throw exception")
    void whenValidatingValidCpfWithFormatting_thenShouldNotThrowException() {
        // Given
        String validCpf = "005.883.809-03";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(validCpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11144477735",
            "52998224725",
            "34608514300",
            "70351457100"
    })
    @DisplayName("When validating multiple valid CPFs Then should not throw exception")
    void whenValidatingMultipleValidCpfs_thenShouldNotThrowException(String validCpf) {
        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(validCpf));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("When validating null or blank document Then should throw UserInvalidDocumentException")
    void whenValidatingNullOrBlankDocument_thenShouldThrowException(String invalidDocument) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidDocument);
        });

        assertEquals(UserErrorMessage.INVALID_DOCUMENT.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_DOCUMENT.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999"
    })
    @DisplayName("When validating CPF with all equal digits Then should throw UserInvalidDocumentException")
    void whenValidatingCpfWithAllEqualDigits_thenShouldThrowException(String invalidCpf) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_CPF.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",
            "1234567890",
            "123456789012",
            "12345678901234"
    })
    @DisplayName("When validating document with wrong length Then should throw UserInvalidDocumentException")
    void whenValidatingDocumentWithWrongLength_thenShouldThrowException(String invalidCpf) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_CPF.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc12345678",
            "1234567890a",
            "12345@67890",
            "123#4567890"
    })
    @DisplayName("When validating document with letters or special chars Then should throw UserInvalidDocumentException")
    void whenValidatingDocumentWithLettersOrSpecialChars_thenShouldThrowException(String invalidCpf) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_CPF.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678900",
            "00588380902",
            "11144477736",
            "52998224726"
    })
    @DisplayName("When validating CPF with invalid first check digit Then should throw UserInvalidDocumentException")
    void whenValidatingCpfWithInvalidFirstCheckDigit_thenShouldThrowException(String invalidCpf) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_CPF.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00588380904",
            "11144477734",
            "52998224720"
    })
    @DisplayName("When validating CPF with invalid second check digit Then should throw UserInvalidDocumentException")
    void whenValidatingCpfWithInvalidSecondCheckDigit_thenShouldThrowException(String invalidCpf) {
        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
        assertEquals(UserErrorMessage.INVALID_CPF.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("When validating CPF with special formatting Then should accept")
    void whenValidatingCpfWithSpecialFormatting_thenShouldAccept() {
        // Given
        String cpf = "005-883-809.03";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(cpf));
    }

    @Test
    @DisplayName("When validating CPF with mixed formatting Then should accept")
    void whenValidatingCpfWithMixedFormatting_thenShouldAccept() {
        // Given
        String cpf = "005.883.80903";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(cpf));
    }

    @Test
    @DisplayName("When validating CPF with spaces Then should accept if valid")
    void whenValidatingCpfWithSpaces_thenShouldAcceptIfValid() {
        // Given
        String cpf = "005 883 809 03";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(cpf));
    }

    @Test
    @DisplayName("When calling static digitValidation with valid first digit Then should not throw exception")
    void whenCallingStaticDigitValidationWithValidFirstDigit_thenShouldNotThrowException() {
        // Given
        int[] numbers = {0, 0, 5, 8, 8, 3, 8, 0, 9, 0, 3};

        // When & Then
        assertDoesNotThrow(() -> {
            DocumentValidationService.digitValidation(numbers, 10, 9);
        });
    }

    @Test
    @DisplayName("When calling static digitValidation with valid second digit Then should not throw exception")
    void whenCallingStaticDigitValidationWithValidSecondDigit_thenShouldNotThrowException() {
        // Given
        int[] numbers = {0, 0, 5, 8, 8, 3, 8, 0, 9, 0, 3};

        // When & Then
        assertDoesNotThrow(() -> {
            DocumentValidationService.digitValidation(numbers, 11, 10);
        });
    }

    @Test
    @DisplayName("When validating CPF with remainder less than 2 Then should use 0 as check digit")
    void whenValidatingCpfWithRemainderLessThan2_thenShouldUse0AsCheckDigit() {
        // Given - CPF that results in remainder < 2 for first check digit
        String cpf = "00000000191";

        // When & Then
        assertDoesNotThrow(() -> service.documentValidation(cpf));
    }

    @Test
    @DisplayName("When exception is thrown Then should have proper error code and message")
    void whenExceptionIsThrown_thenShouldHaveProperErrorCodeAndMessage() {
        // Given
        String invalidCpf = "12345678900";

        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertNotNull(exception.getCode());
        assertNotNull(exception.getMessage());
        assertFalse(exception.getMessage().isEmpty());
        assertFalse(exception.getCode().isEmpty());
    }

    @Test
    @DisplayName("When validating document multiple times Then should be consistent")
    void whenValidatingDocumentMultipleTimes_thenShouldBeConsistent() {
        // Given
        String validCpf = "00588380903";

        // When & Then
        assertDoesNotThrow(() -> {
            service.documentValidation(validCpf);
            service.documentValidation(validCpf);
            service.documentValidation(validCpf);
        });
    }

    @Test
    @DisplayName("When validating document with only dots and dashes Then should throw exception")
    void whenValidatingDocumentWithOnlyDotsAndDashes_thenShouldThrowException() {
        // Given
        String invalidCpf = "...---...--";

        // When & Then
        UserInvalidDocumentException exception = assertThrows(UserInvalidDocumentException.class, () -> {
            service.documentValidation(invalidCpf);
        });

        assertEquals(UserErrorMessage.INVALID_CPF.getCode(), exception.getCode());
    }
}