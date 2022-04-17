package fr.univ.givr.constraint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NameValidatorTest {

    private NameValidator validator;

    @BeforeEach
    void onBefore() {
        validator = new NameValidator();
    }

    @Test
    void isValidIfNullTest() {
        assertThat(validator.isValid(null, Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test",
            "aaaaaa",
            "AAAAAA",
            "abcdefhijklmnopqrstuvwxyz",
            "ABCDEFHIJKLMNOPQRSTUVWXYZ"
    })
    void isValidContainsOnlyLettersTest(String name) {
        assertThat(validator.isValid(name, Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test-Test",
            "Test1",
            "aaaaaa1",
            "AAAAAA1",
            "abcdefhijklmnopqrstuvwxyz1",
            "ABCDEFHIJKLMNOPQRSTUVWXYZ1",
            "X Æ A-12",
            "Test test"
    })
    void isInvalidContainsOtherThanLettersTest(String name) {
        assertThat(validator.isValid(name, Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

}
