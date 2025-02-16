package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestExistsException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupJoinRequestService implements CreateGroupJoinRequestUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupJoinRequestPort groupJoinRequestPort;
  private final UserPort userPort;

  private final GroupEventPublishPort groupEventPublishPort;

  @Transactional
  @Override
  public void creatGroupJoinRequest(GroupJoinRequestCreateCommand command) {
    String groupId = command.getGroupId();
    String userId = command.getUserId();

    Group group = groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);
    validateUserNotParticipants(userId, groupId);
    validateGroupJoinRequestNotExists(userId, groupId);

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);

    GroupJoinRequest groupJoinRequest = GroupJoinRequest.of(userId, groupId);
    groupJoinRequestPort.save(groupJoinRequest);
    groupEventPublishPort.publish(new CreateGroupJoinRequestEvent(group, user));
  }

  private void validateGroupJoinRequestNotExists(String userId, String groupId) {
    if (groupJoinRequestPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new GroupJoinRequestExistsException();
    }
  }

  private void validateUserNotParticipants(String userId, String groupId) {
    if (groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new AlreadyJoinedGroupException();
    }
  }

  private void validateGroupExists(String groupId) {
    if (!groupPort.existsById(groupId)) {
      throw new GroupNotFoundException();
    }
  }

}
