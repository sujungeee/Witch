package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupJoinRequest;
import com.ssafy.witch.group.GroupJoinRequestPort;
import com.ssafy.witch.mapper.group.GroupJoinRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GroupJoinRequestRepository implements GroupJoinRequestPort {

  private final GroupJoinRequestJpaRepository groupJoinRequestJpaRepository;
  private final GroupJoinRequestMapper mapper;

  @Override
  public void save(GroupJoinRequest groupJoinRequest) {
    groupJoinRequestJpaRepository.save(mapper.toEntity(groupJoinRequest));
  }

  @Override
  public boolean existsByUserIdAndGroupId(String userId, String groupId) {
    return groupJoinRequestJpaRepository.existsByUserIdAndGroupId(userId, groupId);
  }
}
