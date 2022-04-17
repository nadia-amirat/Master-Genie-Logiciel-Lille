package fr.univ.givr.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator for {@link Name} constraint annotation.
 */
public class NameValidator implements ConstraintValidator<Name, String> {

    private static final Pattern REGEX = Pattern.compile("^[a-zA-Z]+");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || REGEX.matcher(value).matches();
    }
}
