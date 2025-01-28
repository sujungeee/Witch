package com.ssafy.witch.validate.validator;

import com.ssafy.witch.validate.annotation.WitchNickname;

public class WitchNicknameValidator extends RegexValidator<WitchNickname, String> {

  @Override
  protected String getRegex() {
    return "^[a-zA-Z0-9가-힣]{2,8}$";
  }

  @Override
  protected String getErrorMessage() {
    return "닉네임은 영문 or 숫자 or 한글의 2-8자의 문자열이어야 합니다";
  }
}
