package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.model.AppointmentDetailProjection;
import java.time.Duration;

public interface OnGoingAppointmentCachePort {

  boolean has(String appointmentId);

  void save(AppointmentDetailProjection appointmentDetail, Duration duration);

  AppointmentDetailProjection get(String appointmentId);

  void remove(String appointmentId);
}
