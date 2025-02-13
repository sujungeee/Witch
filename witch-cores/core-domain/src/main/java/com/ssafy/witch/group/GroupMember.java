package com.ssafy.witch.group;


import java.util.UUID;
import lombok.Getter;

@Getter
public class GroupMember {

  private String groupMemberId;

  private String userId;

  private String groupId;

  private boolean isLeader;


  private int cntLateArrival;

  public GroupMember(String groupMemberId, String userId, String groupId, boolean isLeader,
      int cntLateArrival) {
    this.groupMemberId = groupMemberId;
    this.userId = userId;
    this.groupId = groupId;
    this.isLeader = isLeader;
    this.cntLateArrival = cntLateArrival;
  }

  public static GroupMember createNewGroupLeader(String userId, String groupId) {
    return new GroupMember(
        UUID.randomUUID().toString(),
        userId,
        groupId,
        true,
        0
    );
  }

  public static GroupMember createNewGroupMember(String userId, String groupId) {
    return new GroupMember(
        UUID.randomUUID().toString(),
        userId,
        groupId,
        false,
        0
    );
  }
}
