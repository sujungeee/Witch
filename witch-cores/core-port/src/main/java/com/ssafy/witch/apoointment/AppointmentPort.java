package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.Appointment;
import java.time.LocalDateTime;

public interface AppointmentPort {

  Appointment save(Appointment appointment);

  boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime);
}
