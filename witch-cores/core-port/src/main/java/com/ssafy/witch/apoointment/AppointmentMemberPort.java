package com.ssafy.witch.apoointment;

import com.ssafy.witch.appointment.AppointmentMember;

public interface AppointmentMemberPort {

  AppointmentMember save(AppointmentMember appointmentMember);

  boolean existsByUserIdAndAppointmentId(String userId, String appointmentId);
}
