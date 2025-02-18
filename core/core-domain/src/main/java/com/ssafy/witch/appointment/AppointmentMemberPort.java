package com.ssafy.witch.appointment;

import java.util.Optional;

public interface AppointmentMemberPort {

  AppointmentMember save(AppointmentMember appointmentMember);

  boolean existsByUserIdAndAppointmentId(String userId, String appointmentId);

  Optional<AppointmentMember> findByUserIdAndAppointmentId(String userId, String appointmentId);

  void delete(AppointmentMember appointmentMember);

  void deleteAllByAppointmentId(String appointmentId);
}
