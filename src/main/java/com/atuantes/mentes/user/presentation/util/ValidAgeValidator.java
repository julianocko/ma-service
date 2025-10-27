package com.atuantes.mentes.user.presentation.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class ValidAgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidAge constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        
        if (birthdate.isAfter(today)) {
            return false;
        }

        int age = Period.between(birthdate, today).getYears();
        
        return age >= min && age <= max;
    }
}