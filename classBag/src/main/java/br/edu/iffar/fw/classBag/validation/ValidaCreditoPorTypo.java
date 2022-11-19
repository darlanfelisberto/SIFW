package br.edu.iffar.fw.classBag.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidaCreditoPorTypoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidaCreditoPorTypo {

    String message() default "Crédito da origem não pode ser nulo.";

    String typeCreditoPara();

    String realizadoPorCredito();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}