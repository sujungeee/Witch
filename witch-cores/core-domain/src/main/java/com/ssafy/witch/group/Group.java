package com.ssafy.witch.group;

import java.util.UUID;
import lombok.Getter;

@Getter
public class Group {

  private final String groupId;

  private String name;
  private String groupImageUrl;

  public Group(String groupId, String name, String groupImageUrl) {
    this.groupId = groupId;
    this.name = name;
    this.groupImageUrl = groupImageUrl;
  }

  public static Group createNewGroup(String name, String groupImageUrl) {
    return new Group(
        UUID.randomUUID().toString(),
        name,
        groupImageUrl
    );
  }

  public void changeGroupName(String name) {
    this.name = name;
  }
}
