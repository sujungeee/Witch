package com.ssafy.witch.repository.appointment;

import java.time.LocalDateTime;

public interface AppointmentCustomRepository {

  boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime);
}
