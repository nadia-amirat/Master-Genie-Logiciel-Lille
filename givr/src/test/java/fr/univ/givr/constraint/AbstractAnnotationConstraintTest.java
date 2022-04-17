package fr.univ.givr.constraint;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor
abstract class AbstractAnnotationConstraintTest {

    private final Class<? extends Annotation> constraintAnnotationClass;

    private final Class<? extends javax.validation.ConstraintValidator<?, ?>> validatorClass;

    @Test
    void annotationHasCorrespondingValidatorTest() {
        Constraint annotation = constraintAnnotationClass.getAnnotation(Constraint.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.validatedBy()).containsExactly(validatorClass);
    }

}
