package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchPassword;

public class WitchPasswordValidator extends RegexValidator<WitchPassword, String> {

  @Override
  protected String getRegex() {
    return "^[a-zA-Z0-9!@#$%^&*()]{8,16}$";
  }

  @Override
  protected String getErrorMessage() {
    return "패스워드는 영문 or 숫자 or 특수문자[!@#$%^&*]의 8-16자의 문자열이어야 합니다";
  }
}
