package com.ssafy.witch.apoointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentMemberProjection {

  private String userId;
  private String nickname;
  private String profileImageUrl;
  private boolean isLeader;
}
