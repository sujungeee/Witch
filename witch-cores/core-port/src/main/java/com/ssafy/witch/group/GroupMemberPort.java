package com.ssafy.witch.group;

public interface GroupMemberPort {

  GroupMember save(GroupMember groupMember);

  boolean existsByUserIdAndGroupId(String userId, String groupId);
}
