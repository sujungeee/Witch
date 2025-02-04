package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.GroupMember;
import com.ssafy.witch.group.GroupMemberPort;
import com.ssafy.witch.mapper.group.GroupMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GroupMemberRepository implements GroupMemberPort {

  private final GroupMemberJpaRepository groupMemberJpaRepository;

  private final GroupMemberMapper mapper;

  @Override
  public GroupMember save(GroupMember groupMember) {
    return mapper.toDomain(groupMemberJpaRepository.save(mapper.toEntity(groupMember)));
  }
}
