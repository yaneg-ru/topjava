package ru.javawebinar.topjava.util.validate;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueFieldValidater implements ConstraintValidator<UniqueEmail, Object> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        boolean allRight = true;

        UserTo userTo = (UserTo) value;
        User user = repository.getByEmail(userTo.getEmail());

        try {
            int authUserUd = SecurityUtil.authUserId();
            if ((userTo.isNew() && user!=null && user.id()!=authUserUd) ||
                    (!userTo.isNew()) && user!=null && userTo.id()!=user.id()) {
                allRight = false;
            }
        } catch (Exception e) {
            if (user!=null) {
                allRight = false;

            }
        }

        if (!allRight) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "User with this email already exists" )
                    .addPropertyNode( "email" ).addConstraintViolation();
        }

        return allRight;
    }
}
