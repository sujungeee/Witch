package com.ssafy.witch.validate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WitchNickname {

  String message() default "닉네임은 영문 or 숫자 or 한글의 2-8자의 문자열이어야 합니다";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
