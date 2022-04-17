package fr.univ.givr.constraint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoleValidatorTest {

    private RoleValidator validator;

    @BeforeEach
    void onBefore() {
        validator = new RoleValidator();
    }

    @Test
    void isValidIfNullTest() {
        assertThat(validator.isValid(null, Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "TEST",
            "ROLETEST"
    })
    void isInvalidIfNotPrefixedByRoleTest(String role) {
        assertThat(validator.isValid(role, Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ROLE_1",
            "ROLE_test",
            "ROLE_&",
            "ROLE_TEST_",
            "ROLE_TEST_TEST"
    })
    void isInvalidIfHaveOtherThanUpperTest(String role) {
        assertThat(validator.isValid(role, Mockito.mock(ConstraintValidatorContext.class))).isFalse();
    }

    void isValidRoleWithOnlyUpperPrefixedByRoleTest() {
        assertThat(validator.isValid("ROLE_TEST", Mockito.mock(ConstraintValidatorContext.class))).isTrue();
    }

}
