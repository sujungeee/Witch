package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;

public interface AppointmentJoinEventPublishPort {

  void publish(AppointmentJoinEvent appointmentJoinEvent);

}
