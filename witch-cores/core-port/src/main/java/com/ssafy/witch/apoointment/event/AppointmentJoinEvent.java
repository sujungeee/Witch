package com.ssafy.witch.apoointment.event;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentJoinEvent {

  private String joinUserId;
  private AppointmentDetailProjection appointmentDetail;

}
