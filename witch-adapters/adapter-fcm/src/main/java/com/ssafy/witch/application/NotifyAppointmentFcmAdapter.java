package com.ssafy.witch.application;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.event.AppointmentEvent;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import com.ssafy.witch.user.UserNotification;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyAppointmentFcmAdapter implements NotifyAppointmentPort {

  private final FcmNotificator fcmNotificator;

  private String getBody(AppointmentJoinNotification appointmentJoinNotification) {
    List<UserNotification> members = appointmentJoinNotification.getMembers();
    String name = appointmentJoinNotification.getName();
    String joinUserId = appointmentJoinNotification.getJoinUserId();
    UserNotification joinUser = members.stream()
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

    Map<String, String> data = Map.of("type", AppointmentEvent.JOIN_APPOINTMENT, "appointmentId",
        appointmentId);

    String joinUserId = appointmentJoinNotification.getJoinUserId();
    for (UserNotification member : appointmentJoinNotification.getMembers()) {
      if (member.getUserId().equals(joinUserId)) {
        continue;
      }

      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyStartAppointment(AppointmentStartNotification appointmentStartNotification) {

    String title = "[Witch] 약속 시작 알림";

    LocalDateTime appointmentTime = appointmentStartNotification.getAppointmentTime();
    String body = String.format("%s시에 시작하는 %s 약속 공유가 시작되었습니다.",
        appointmentTime.format(DateTimeFormatter.ofPattern("HH:mm")),
        appointmentStartNotification.getName());

    Map<String, String> data = Map.of("type", AppointmentEvent.JOIN_APPOINTMENT, "appointmentId",
        appointmentStartNotification.getAppointmentId());

    for (UserNotification member : appointmentStartNotification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }
}
