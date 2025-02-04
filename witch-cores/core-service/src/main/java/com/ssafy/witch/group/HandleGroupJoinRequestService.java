package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandleGroupJoinRequestService implements HandleGroupJoinRequestUseCase {

  private final GroupJoinRequestPort groupJoinRequestPort;
  private final GroupMemberPort groupMemberPort;

  @Value("${witch.group.max-participants-count}")
  private int maxGroupParticipantsCount;

  @Override
  public void approveGroupJoinRequest(ApproveGroupJoinRequestCommand command) {
    GroupJoinRequest groupJoinRequest = groupJoinRequestPort.findById(command.getJoinRequestId())
        .orElseThrow(GroupJoinRequestNotFoundException::new);

    String groupId = groupJoinRequest.getGroupId();
    String requestUserId = groupJoinRequest.getUserId();

    List<GroupMember> groupMembers = groupMemberPort.findAllByGroupId(groupId);

    validateUserNotParticipants(requestUserId, groupMembers);
    validateLeaderAuthorization(groupMembers, command.getUserId());
    validateGroupCapacity(groupMembers);

    groupJoinRequestPort.deleteById(command.getJoinRequestId());
    groupMemberPort.save(GroupMember.createNewGroupMember(groupJoinRequest.getUserId(), groupId));
  }

  private void validateLeaderAuthorization(List<GroupMember> groupMembers, String userId) {
    boolean isLeader = groupMembers.stream()
        .anyMatch(member -> member.getUserId().equals(userId) && member.getIsLeader());

    if (!isLeader) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateGroupCapacity(List<GroupMember> groupMembers) {
    if (groupMembers.size() >= maxGroupParticipantsCount) {
      throw new UnauthorizedGroupAccessException();
    }
  }

  private void validateUserNotParticipants(String requestUserId, List<GroupMember> groupMembers) {
    boolean isParticipant = groupMembers.stream()
        .anyMatch(member -> member.getUserId().equals(requestUserId));

    if (isParticipant) {
      throw new AlreadyJoinedGroupException();
    }
  }
}
