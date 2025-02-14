package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import java.time.Duration;

public interface OnGoingAppointmentCachePort {

  boolean has(AppointmentDetailProjection appointment);

  void save(AppointmentDetailProjection appointmentDetail, Duration duration);
}
