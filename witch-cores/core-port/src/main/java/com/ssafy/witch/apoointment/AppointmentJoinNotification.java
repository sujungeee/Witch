package com.ssafy.witch.apoointment;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentJoinNotification {

  private String appointmentId;
  private String name;
  private String joinUserId;
  private List<User> members;


  @AllArgsConstructor
  @Getter
  public static class User {

    private String userId;
    private String nickname;
    private String fcmToken;
  }
}
