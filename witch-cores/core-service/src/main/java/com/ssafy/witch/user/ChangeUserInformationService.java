package com.ssafy.witch.user;

import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChangeUserInformationService implements ChangeUserInformationUseCase {

  private final ValidateUserUseCase validateUserUseCase;
  private final UserPort userPort;

  @Transactional
  @Override
  public void changeUserNickname(ChangeUserNicknameCommand command) {
    String userId = command.getUserId();
    String nickname = command.getNickname();

    validateUserUseCase.checkUserNicknameDuplication(nickname);

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);
    user.changeNickname(nickname);

    userPort.save(user);
  }
}
