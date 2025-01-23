package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserValidateService implements ValidateUserUseCase {

  private final ValidateUserPort validateUserPort;

  @Override
  public void checkUserEmailDuplication(final String email) {
    if (validateUserPort.isEmailDuplicated(email)) {
      throw new UserEmailDuplicatedException();
    }
  }

}
