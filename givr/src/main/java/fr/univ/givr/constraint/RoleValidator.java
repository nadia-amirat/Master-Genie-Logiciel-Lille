package fr.univ.givr.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator for {@link Role} constraint annotation.
 */
public class RoleValidator implements ConstraintValidator<Role, String> {

    private static final Pattern REGEX = Pattern.compile("^ROLE_[A-Z]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || REGEX.matcher(value).matches();
    }
}
