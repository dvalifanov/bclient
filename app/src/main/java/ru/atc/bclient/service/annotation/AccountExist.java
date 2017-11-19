package ru.atc.bclient.service.annotation;

import ru.atc.bclient.service.validator.AccountExistValidator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { AccountExistValidator.class })
public @interface AccountExist {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}