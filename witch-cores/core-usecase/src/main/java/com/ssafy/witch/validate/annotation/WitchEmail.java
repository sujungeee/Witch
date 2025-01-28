package com.ssafy.witch.validate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@NotBlank
@Email
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WitchEmail {

  String message() default "올바른 형식의 이메일 주소여야 합니다";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
