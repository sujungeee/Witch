package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.Appointment;

public interface AppointmentPort {

  Appointment save(Appointment appointment);

  boolean hasOngoingAppointment(String userId);
}
