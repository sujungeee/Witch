package com.ssafy.witch.user;

import com.ssafy.witch.user.command.DeleteUserCommand;

public interface DeleteUserUseCase {

  void delete(DeleteUserCommand command);

}
