package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestExistsException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupJoinRequestService implements CreateGroupJoinRequestUseCase {

  private final GroupPort groupPort;
  private final GroupMemberPort groupMemberPort;
  private final GroupJoinRequestPort groupJoinRequestPort;

  @Transactional
  @Override
  public void creatGroupJoinRequest(GroupJoinRequestCreateCommand command) {
    String groupId = command.getGroupId();
    String userId = command.getUserId();

    validateGroupExists(groupId);
    validateUserNotParticipants(userId, groupId);
    validateGroupJoinRequestNotExists(userId, groupId);

    GroupJoinRequest groupJoinRequest = GroupJoinRequest.of(userId, groupId);
    groupJoinRequestPort.save(groupJoinRequest);
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
