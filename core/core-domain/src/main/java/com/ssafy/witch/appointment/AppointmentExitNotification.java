package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.model.AppointmentDetailProjection;
import com.ssafy.witch.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentExitNotification {

  private User exitUser;
  private AppointmentDetailProjection appointment;

}
