package com.ssafy.witch.repository.group;

public interface GroupMemberCustomRepository {

  boolean isLeaderByUserIdAndGroupId(String userId, String groupId);
}
