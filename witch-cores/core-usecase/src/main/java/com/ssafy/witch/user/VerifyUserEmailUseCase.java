package com.ssafy.witch.user;

import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;

public interface VerifyUserEmailUseCase {

  void createUserEmailVerificationCode(CreateUserEmailVerificationCodeCommand command);

}
