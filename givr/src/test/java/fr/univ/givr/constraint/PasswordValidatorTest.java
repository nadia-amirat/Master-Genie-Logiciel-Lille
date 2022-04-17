package fr.univ.givr.constraint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void onBefore() {
        validator = new PasswordValidator();
    }

    @Test
    void isValidIfNullTest() {
        assertThat(validator.isValid(null, Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

    @Test
    void isInvalidWithLess8CharTest() {
        assertThat(validator.isValid("Azert1&", Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    @Test
    void isInvalidWithNoLowerTest() {
        assertThat(validator.isValid("AZERTY1&", Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    @Test
    void isInvalidWithNoUpperTest() {
        assertThat(validator.isValid("azerty1&", Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    @Test
    void isInvalidWithNoNumberTest() {
        assertThat(validator.isValid("Azertyu&", Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    @Test
    void isValidWithNoSpecialCharTest() {
        assertThat(validator.isValid("Azertyu1", Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Testtes1", // 1 Upper + 1 number + length = 8
            "TESTTEs1", // 1 Lower + 1 number + length = 8
            "TestTest12", // 2 Upper + 2 number + length > 8
            "TEST&test1", // 4 Upper + 4 Lower + 1 number + 1 special + length > 8
            "TEST test1", // 4 Upper + 4 Lower + 1 number + 1 special + length > 8
    })
    void isValidWithAtLeastOneUpperAndNumberWithLengthEqualsOrGreaterThan8(String name) {
        assertThat(validator.isValid(name, Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }
}
