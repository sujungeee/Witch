package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.model.AppointmentDetailProjection;
import com.ssafy.witch.appointment.model.AppointmentProjection;
import com.ssafy.witch.appointment.model.AppointmentWithGroupProjection;
import java.util.List;

public interface AppointmentReadPort {

  List<AppointmentProjection> getAppointments(String userId, String groupId);

  List<AppointmentWithGroupProjection> getMyAppointment(String userId, int year, int month);

  AppointmentDetailProjection getAppointmentDetail(String appointmentId);
}
