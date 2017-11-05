package com.expercise.interpreterapi.request.validator;

import com.expercise.interpreterapi.request.ProgrammingLanguage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProgrammingLanguageValidator implements ConstraintValidator<ValidProgrammingLanguage, String> {

    public void initialize(ValidProgrammingLanguage constraint) {
    }

    public boolean isValid(String lang, ConstraintValidatorContext context) {
        try {
            ProgrammingLanguage.valueOf(lang);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
