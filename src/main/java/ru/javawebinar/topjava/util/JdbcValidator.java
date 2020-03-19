package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import javax.validation.*;
import java.util.Set;

public class JdbcValidator<T> {

    private static final Logger log = LoggerFactory.getLogger(JdbcValidator.class);

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private Validator validator = validatorFactory.getValidator();

    public boolean validate(T object) {

        User oldValue = null;

        if (object instanceof Meal) {
            oldValue = ((Meal) object).getUser();
            ((Meal) object).setUser(new User());
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            violations.forEach(item -> {
                log.error(item.getPropertyPath() +" - " + item.getMessage());
            });

            if (object instanceof Meal) {
                ((Meal) object).setUser(oldValue);
            }

            throw new ConstraintViolationException(violations);
        }

        if (object instanceof Meal) {
            ((Meal) object).setUser(oldValue);
        }

        return true;
    }

}
