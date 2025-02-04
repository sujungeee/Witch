package com.ssafy.witch.group;

public interface GroupJoinRequestPort {

  void save(GroupJoinRequest groupJoinRequest);

  boolean existsByUserIdAndGroupId(String userId, String groupId);
}
