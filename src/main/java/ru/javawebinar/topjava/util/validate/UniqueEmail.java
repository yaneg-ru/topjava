package ru.javawebinar.topjava.util.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueFieldValidater.class)
@Documented
public @interface UniqueEmail {
    String message() default "{fields.constrains.email.unique}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
