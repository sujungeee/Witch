package com.ssafy.witch.group.event;

import com.ssafy.witch.group.Group;
import com.ssafy.witch.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateGroupJoinRequestEvent {

  private Group group;
  private User requestUser;
}
