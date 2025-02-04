package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupReadService implements GroupReadUseCase {

  private final GroupPort groupPort;

  @Transactional(readOnly = true)
  @Override
  public Group getGroupPreview(String groupId) {
    return groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);
  }

}
