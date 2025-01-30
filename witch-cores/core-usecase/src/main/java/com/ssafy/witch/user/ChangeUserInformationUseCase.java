package com.ssafy.witch.user;

import com.ssafy.witch.user.command.ChangeUserNicknameCommand;

public interface ChangeUserInformationUseCase {

  void changeUserNickname(ChangeUserNicknameCommand command);

}
