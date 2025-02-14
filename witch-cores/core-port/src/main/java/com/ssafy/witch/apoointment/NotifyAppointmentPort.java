package com.ssafy.witch.apoointment;

public interface NotifyAppointmentPort {

  void notifyJoinAppointment(AppointmentJoinNotification appointmentJoinNotification);

  void notifyStartAppointment(AppointmentStartNotification appointmentStartNotification);
}
