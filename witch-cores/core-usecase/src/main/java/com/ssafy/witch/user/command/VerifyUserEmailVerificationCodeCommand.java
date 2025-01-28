package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyUserEmailVerificationCodeCommand extends
    SelfValidating<VerifyUserEmailVerificationCodeCommand> {

  @NotBlank
  @Email
  private String email;

  @Pattern(regexp = "^\\d{6}$", message = "이메일 인증 코드는 6자리 숫자여야 합니다.")
  private String emailVerificationCode;

  public VerifyUserEmailVerificationCodeCommand(String email, String emailVerificationCode) {
    this.email = email;
    this.emailVerificationCode = emailVerificationCode;

    this.validateSelf();
  }

}
