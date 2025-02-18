package com.ssafy.witch.appointment.command;

import com.ssafy.witch.appointment.AppointmentExitNotification;
import com.ssafy.witch.appointment.event.AppointmentExitEvent;
import com.ssafy.witch.appointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotifyAppointmentExitCommand {

  private User exitUser;
  private AppointmentDetailProjection appointment;

  public NotifyAppointmentExitCommand(AppointmentExitEvent event) {
    this.exitUser = event.getExitUser();
    this.appointment = event.getAppointment();
  }

  public AppointmentExitNotification toNotification() {
    return new AppointmentExitNotification(this.exitUser, this.appointment);
  }

}
