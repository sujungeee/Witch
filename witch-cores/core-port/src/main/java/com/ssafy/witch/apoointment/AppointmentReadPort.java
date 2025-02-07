package com.ssafy.witch.apoointment;

import com.ssafy.witch.apoointment.model.AppointmentProjection;
import java.util.List;

public interface AppointmentReadPort {

  List<AppointmentProjection> getAppointments(String userId, String groupId);
}
