package com.ssafy.witch.group;

import java.util.List;

public interface GroupMemberPort {

  GroupMember save(GroupMember groupMember);

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  List<GroupMember> findAllByGroupId(String groupId);
}
