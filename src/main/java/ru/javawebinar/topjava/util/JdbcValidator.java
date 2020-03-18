package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
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
            throw new ConstraintViolationException(violations);
        }
        return true;
    }

}
