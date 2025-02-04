package com.ssafy.witch.group;

import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;

public interface HandleGroupJoinRequestUseCase {

  void approveGroupJoinRequest(ApproveGroupJoinRequestCommand command);

}
