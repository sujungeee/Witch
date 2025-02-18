package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.event.AppointmentArrivalEvent;
import com.ssafy.witch.appointment.event.AppointmentCreatedEvent;
import com.ssafy.witch.appointment.event.AppointmentEndEvent;
import com.ssafy.witch.appointment.event.AppointmentExitEvent;
import com.ssafy.witch.appointment.event.AppointmentJoinEvent;
import com.ssafy.witch.appointment.event.AppointmentStartEvent;

public interface AppointmentEventPublishPort {

  void publish(AppointmentJoinEvent event);

  void publish(AppointmentStartEvent event);

  void publish(AppointmentEndEvent event);

  void publish(AppointmentArrivalEvent event);

  void publish(AppointmentCreatedEvent event);

  void publish(AppointmentExitEvent event);
}
