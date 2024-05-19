package com.davifaustino.productregistrationsystem.api.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CodeValidator implements ConstraintValidator<ProductCode, String> {

    /**
     * This method verifies code integrity by calculating a check digit in accordance with GS1
     */
    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {

        //Allow code to be null
        if (code == null) { return true; }

        //Check if the entered string is a number
        code = code.replace(" ", "");

        try {
            Long.parseLong(code);

        } catch (NumberFormatException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The code must contain only numbers").addConstraintViolation();

            return false;
        }

        //Calculate the sum of multiplications made by 1 and 3
        String[] digits = code.split("");
        int digitsSum = 0;
        final int lastDigitsIndex = digits.length - 1;

        for (int i = lastDigitsIndex - 1; i >= 0; i--) {
            int multiplicationFactor = (lastDigitsIndex - i) % 2 == 1 ? 3 : 1;
            digitsSum += Integer.parseInt(digits[i]) * multiplicationFactor;
        }

        //Calculate check digit
        int checkDigit;
        if (digitsSum % 10 == 0) {
            checkDigit = 0;
        } else {
            checkDigit = (digitsSum / 10 * 10 + 10) - digitsSum;
        }

        //Check if the check digit is correct
        if (String.valueOf(checkDigit).equals(digits[lastDigitsIndex])) {
            return true;
        }

        return false;
    }
}
