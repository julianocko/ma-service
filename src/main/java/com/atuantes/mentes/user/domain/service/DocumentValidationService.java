package com.atuantes.mentes.user.domain.service;


import com.atuantes.mentes.user.domain.exception.UserInvalidDocumentException;
import com.atuantes.mentes.user.domain.message.UserErrorMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentValidationService {

    public static void documentValidation(String document) throws UserInvalidDocumentException {
        if (document == null || document.isBlank()) {
            throw new UserInvalidDocumentException(UserErrorMessage.INVALID_DOCUMENT.getCode(),
                    UserErrorMessage.INVALID_DOCUMENT.getMessage());
        }
        documentVerification(document);
    }

    private static void documentVerification(String document) throws UserInvalidDocumentException {
        String digits = document.replaceAll("[^a-zA-Z0-9]", "");

        if (digits.length() != 11) {
            throw new UserInvalidDocumentException(UserErrorMessage.INVALID_CPF.getCode(),
                    UserErrorMessage.INVALID_CPF.getMessage());
        }

        // Regex to check if document contains only numbers after cleaning
        if (!digits.matches("\\d{11}")) {
            throw new UserInvalidDocumentException(UserErrorMessage.INVALID_CPF.getCode(),
                    UserErrorMessage.INVALID_CPF.getMessage());
        }

        // Reject sequences with all equal digits (e.g., 00000000000)
        char firstChar = digits.charAt(0);
        boolean allEqual = true;
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != firstChar) {
                allEqual = false;
                break;
            }
        }
        if (allEqual) {
            throw new UserInvalidDocumentException(UserErrorMessage.INVALID_CPF.getCode(),
                    UserErrorMessage.INVALID_CPF.getMessage());
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = digits.charAt(i) - '0';
        }

        // First check digit
        digitValidation(numbers, 10, 9);

        // Second check digit
        digitValidation(numbers, 11, 10);
    }

    public static void digitValidation(int[] numbers, int initialWeight, int validatorPosition) throws UserInvalidDocumentException {
        int sum = 0;
        for (int i = 0, weight = initialWeight; i < validatorPosition; i++, weight--) {
            sum += numbers[i] * weight;
        }
        int remainder = sum % 11;
        int checkDigit1 = (remainder < 2) ? 0 : 11 - remainder;
        if (numbers[validatorPosition] != checkDigit1) {
            throw new UserInvalidDocumentException(UserErrorMessage.INVALID_CPF.getCode(),
                    UserErrorMessage.INVALID_CPF.getMessage());
        }
    }
}
