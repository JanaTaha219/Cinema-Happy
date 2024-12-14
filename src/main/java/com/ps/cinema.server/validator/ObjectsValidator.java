package com.ps.cinema.server.validator;

import com.ps.cinema.server.exception.GeneralException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectsValidator<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public void validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violation = validator.validate(objectToValidate);
        if (!violation.isEmpty()) {
            Set<String> errorMessages = violation
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            throw new GeneralException(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
