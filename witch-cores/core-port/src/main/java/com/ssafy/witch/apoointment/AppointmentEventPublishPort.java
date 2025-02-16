package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.event.AppointmentEndEvent;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.apoointment.event.AppointmentStartEvent;

public interface AppointmentEventPublishPort {

  void publish(AppointmentJoinEvent appointmentJoinEvent);

  void publish(AppointmentStartEvent appointmentStartEvent);

  void publish(AppointmentEndEvent event);
}
