package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupPort;
import com.ssafy.witch.group.ValidateGroupPort;
import com.ssafy.witch.mapper.group.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GroupRepository implements GroupPort, ValidateGroupPort {

  private final GroupJpaRepository groupJpaRepository;

  private final GroupMapper mapper;

  @Override
  public Group save(Group group) {
    return mapper.toDomain(groupJpaRepository.save(mapper.toEntity(group)));
  }

  @Override
  public boolean isNameDuplicated(String name) {
    return groupJpaRepository.existsByName(name);
  }
}
