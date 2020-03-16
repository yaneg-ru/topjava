package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JdbcValidator<T> {

    private static final Logger log = LoggerFactory.getLogger(JdbcValidator.class);

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();

    public boolean validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            violations.forEach(item -> {
                log.error(item.getPropertyPath() +" - " + item.getMessage());
            });
            return false;
        }
        return true;
    }

}
