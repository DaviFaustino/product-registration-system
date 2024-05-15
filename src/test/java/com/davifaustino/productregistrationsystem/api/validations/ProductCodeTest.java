package com.davifaustino.productregistrationsystem.api.validations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ProductCodeTest {

    public class InnerTestClass {

        @ProductCode
        private String code;

        public InnerTestClass(String code) {
            this.code = code;
        }
    }

    private InnerTestClass testClass;

    @Test
    @DisplayName("Validation must not be violated")
    void productCodeTest1() {
        testClass = new InnerTestClass("11111115");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<InnerTestClass>> violations = validator.validate(testClass);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Validation must not be violated")
    void productCodeTest2() {
        testClass = new InnerTestClass("7896354627580");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<InnerTestClass>> violations = validator.validate(testClass);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Validation must be violated")
    void productCodeTest3() {
        testClass = new InnerTestClass("11111116");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<InnerTestClass>> violations = validator.validate(testClass);

        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().equals("Invalid code"));
    }

    @Test
    @DisplayName("Validation must be violated")
    void productCodeTest4() {
        testClass = new InnerTestClass("  a1111115");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<InnerTestClass>> violations = validator.validate(testClass);

        assertFalse(violations.isEmpty());
        assertTrue(violations.iterator().next().getMessage().equals("The code must contain only numbers"));
    }
}
