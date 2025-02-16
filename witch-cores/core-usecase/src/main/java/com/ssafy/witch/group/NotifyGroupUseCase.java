package com.ssafy.witch.group;

import com.ssafy.witch.group.command.NotifyGroupJoinRequestApproveCommand;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestCommand;

public interface NotifyGroupUseCase {

  void notifyJoinRequest(NotifyGroupJoinRequestCommand command);

  void notifyJoinRequestApproved(NotifyGroupJoinRequestApproveCommand command);
}
