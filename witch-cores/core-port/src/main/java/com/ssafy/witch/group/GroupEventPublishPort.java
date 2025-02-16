package com.ssafy.witch.group;

import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;

public interface GroupEventPublishPort {

  void publish(CreateGroupJoinRequestEvent event);
}
