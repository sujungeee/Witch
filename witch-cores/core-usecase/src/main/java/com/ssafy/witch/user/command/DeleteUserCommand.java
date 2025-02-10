package com.ssafy.witch.user.command;

import com.ssafy.witch.validate.SelfValidating;
import com.ssafy.witch.validate.annotation.WitchPassword;
import lombok.Getter;

@Getter
public class DeleteUserCommand extends SelfValidating<DeleteUserCommand>{

  private final String userId;

  @WitchPassword
  private final String password;

  private final String refreshToken;

  public DeleteUserCommand(String userId, String password, String refreshToken) {
    this.userId = userId;
    this.password = password;
    this.refreshToken = refreshToken;

    this.validateSelf();
  }
}
