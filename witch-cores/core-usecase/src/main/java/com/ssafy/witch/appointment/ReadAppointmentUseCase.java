package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.output.AppointmentListOutput;

public interface ReadAppointmentUseCase {

  AppointmentListOutput getAppointments(String userId, String groupId);

}
