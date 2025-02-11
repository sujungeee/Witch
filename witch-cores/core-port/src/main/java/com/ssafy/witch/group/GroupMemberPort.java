package com.ssafy.witch.group;

import com.ssafy.witch.appointment.AppointmentMember;

import java.util.List;
import java.util.Optional;

public interface GroupMemberPort {

  GroupMember save(GroupMember groupMember);

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  List<GroupMember> findAllByGroupId(String groupId);

  boolean isLeaderByUserIdAndGroupId(String userId, String groupId);

  void deleteMember(String userId, String groupId);

  void deleteAllByGroupId(String groupId);

  Optional<GroupMember> findByUserIdAndGroupId(String userId, String groupId);
}
