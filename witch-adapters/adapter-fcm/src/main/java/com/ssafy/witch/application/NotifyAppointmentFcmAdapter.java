package com.ssafy.witch.application;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentJoinNotification.User;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyAppointmentFcmAdapter implements NotifyAppointmentPort {

  private final FcmNotificator fcmNotificator;

  private String getBody(AppointmentJoinNotification appointmentJoinNotification) {
    List<User> members = appointmentJoinNotification.getMembers();
    String name = appointmentJoinNotification.getName();
    String joinUserId = appointmentJoinNotification.getJoinUserId();
    User joinUser = members.stream()
        .filter(u -> u.getUserId().equals(joinUserId))
        .findFirst()
        .orElseThrow();
    String joinUserNickname = joinUser.getNickname();
    return String.format("%s 약속에 %s님이 참여하였습니다.", name, joinUserNickname);
  }

  @Override
  public void notifyJoinAppointment(AppointmentJoinNotification appointmentJoinNotification) {
    String appointmentId = appointmentJoinNotification.getAppointmentId();

    String title = "[Witch] 약속 참여 알림";
    String body = getBody(appointmentJoinNotification);

    Map<String, String> data = Map.of("appointmentId", appointmentId);

    String joinUserId = appointmentJoinNotification.getJoinUserId();
    for (User member : appointmentJoinNotification.getMembers()) {
      if (member.getUserId().equals(joinUserId)) {
        continue;
      }

      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }
}
