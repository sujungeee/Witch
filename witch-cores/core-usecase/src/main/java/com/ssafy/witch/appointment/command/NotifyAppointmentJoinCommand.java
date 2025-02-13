package com.ssafy.witch.appointment.command;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentJoinCommand {

  private String appointmentId;
  private String name;
  private String joinUserId;
  private List<User> members;

  public NotifyAppointmentJoinCommand(AppointmentJoinEvent event) {
    AppointmentDetailProjection appointment = event.getAppointment();
    this.appointmentId = appointment.getAppointmentId();
    this.name = appointment.getName();

    this.joinUserId = event.getJoinUserId();

    this.members = appointment.getMembers()
        .stream()
        .map(member -> new User(member.getUserId(), member.getNickname(), member.getFcmToken()))
        .toList();
  }

  public AppointmentJoinNotification toNotification(NotifyAppointmentJoinCommand command) {
    return new AppointmentJoinNotification(
        command.getAppointmentId(), command.getName(), command.getJoinUserId(),
        command.members.stream().map(NotifyAppointmentJoinCommand.User::toCommand).toList()
    );
  }

  @AllArgsConstructor
  @Getter
  private static class User {

    private String userId;
    private String nickname;
    private String fcmToken;

    public AppointmentJoinNotification.User toCommand() {
      return new AppointmentJoinNotification.User(this.userId, this.nickname, this.fcmToken);
    }
  }

}
