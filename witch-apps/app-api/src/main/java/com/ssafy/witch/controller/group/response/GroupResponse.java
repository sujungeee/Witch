package com.ssafy.witch.controller.group.response;

import com.ssafy.witch.controller.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupResponse {

  private String groupId;
  private String name;
  private String groupImageUrl;
  private String createdAt;
  private UserResponse leader;

}
