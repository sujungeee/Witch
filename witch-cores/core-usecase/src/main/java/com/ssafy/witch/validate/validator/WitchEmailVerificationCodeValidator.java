package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchEmailVerificationCode;

public class WitchEmailVerificationCodeValidator extends
    RegexValidator<WitchEmailVerificationCode, String> {

  @Override
  protected String getRegex() {
    return "^\\d{6}$";
  }

  @Override
  protected String getErrorMessage() {
    return "이메일 인증 코드는 6자리 숫자여야 합니다";
  }
}
