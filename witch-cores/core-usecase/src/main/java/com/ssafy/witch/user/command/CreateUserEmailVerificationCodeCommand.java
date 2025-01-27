package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateUserEmailVerificationCodeCommand extends
    SelfValidating<CreateUserEmailVerificationCodeCommand> {

  @NotBlank
  @Email
  private final String email;

  public CreateUserEmailVerificationCodeCommand(String email) {
    this.email = email;
    this.validateSelf();
  }
}
