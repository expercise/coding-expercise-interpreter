package com.expercise.interpreterapi.request.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProgrammingLanguageValidator.class)
@Documented
public @interface ValidProgrammingLanguage {

    String message() default "{programmingLanguage.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}