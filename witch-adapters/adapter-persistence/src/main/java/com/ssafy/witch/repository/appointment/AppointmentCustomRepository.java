package com.ssafy.witch.repository.appointment;

public interface AppointmentCustomRepository {

  boolean existsOngoingAppointmentByUserId(String userId);
}
