package com.ssafy.witch.group;

import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import com.ssafy.witch.group.command.RejectGroupJoinRequestCommand;

public interface HandleGroupJoinRequestUseCase {

  void approveGroupJoinRequest(ApproveGroupJoinRequestCommand command);

  void rejectGroupJoinRequest(RejectGroupJoinRequestCommand command);

}
