package com.ssafy.witch.application;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.event.AppointmentEvent;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import com.ssafy.witch.user.UserNotification;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyAppointmentFcmAdapter implements NotifyAppointmentPort {

  private final FcmNotificator fcmNotificator;


  @Override
  public void notifyJoinAppointment(AppointmentJoinNotification appointmentJoinNotification) {
    String appointmentId = appointmentJoinNotification.getAppointmentId();
    List<UserNotification> members = appointmentJoinNotification.getMembers();
    String name = appointmentJoinNotification.getName();
    String joinUserId = appointmentJoinNotification.getJoinUserId();
    UserNotification joinUser = members.stream()
        .filter(u -> u.getUserId().equals(joinUserId))
        .findFirst()
        .orElseThrow();
    String joinUserNickname = joinUser.getNickname();

    String title = String.format("[Witch] %s 약속에 %s 님이 참여했어요.", name, joinUserNickname);
    String body = "약속 참여자 목록을 확인해 보세요!";

    Map<String, String> data = Map.of("type", AppointmentEvent.JOIN_APPOINTMENT, "parameter",
        appointmentId);

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

    String appointmentName = appointmentStartNotification.getName();

    String title = String.format("[Witch] %s 약속이 시작되었어요", appointmentName);
    String body = "약속 참여자들의 위치를 확인해 보세요!";

    Map<String, String> data = Map.of("type", AppointmentEvent.JOIN_APPOINTMENT, "parameter",
        appointmentStartNotification.getAppointmentId());

    for (UserNotification member : appointmentStartNotification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyEndAppointment(AppointmentStartNotification notification) {
    String title = String.format("[Witch] %s 약속이 종료되었어요.", notification.getName());
    String body = "약속 참여자들의 최종 위치를 확인해 보세요!";

    Map<String, String> data = Map.of("type", AppointmentEvent.END_APPOINTMENT, "parameter",
        notification.getAppointmentId());

    for (UserNotification member : notification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

}
