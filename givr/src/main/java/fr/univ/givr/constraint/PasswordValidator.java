package fr.univ.givr.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator for {@link Password} constraint annotation.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Pattern REGEX = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || REGEX.matcher(value).matches();
    }
}
