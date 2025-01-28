package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchEmail;

public class WitchEmailValidator extends RegexValidator<WitchEmail, String> {

  @Override
  protected String getRegex() {
    return "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  }

  @Override
  protected String getErrorMessage() {
    return "올바른 형식의 이메일 주소여야 합니다";
  }
}
