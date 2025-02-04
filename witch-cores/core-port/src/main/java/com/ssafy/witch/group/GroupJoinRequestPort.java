package com.ssafy.witch.group;

import java.util.Optional;

public interface GroupJoinRequestPort {

  void save(GroupJoinRequest groupJoinRequest);

  boolean existsByUserIdAndGroupId(String userId, String groupId);

  Optional<GroupJoinRequest> findById(String groupJoinRequestId);

  void deleteById(String groupJoinRequestId);
}
