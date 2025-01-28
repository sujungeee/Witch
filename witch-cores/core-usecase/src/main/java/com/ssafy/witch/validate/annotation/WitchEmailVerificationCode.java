package com.ssafy.witch.validate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Pattern(regexp = "^\\d{6}$")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WitchEmailVerificationCode {

  String message() default "이메일 인증 코드는 6자리 숫자여야 합니다";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
