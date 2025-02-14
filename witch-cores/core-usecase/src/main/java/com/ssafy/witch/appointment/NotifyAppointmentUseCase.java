package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentStartCommand;

public interface NotifyAppointmentUseCase {

  void notifyJoin(NotifyAppointmentJoinCommand command);

  void notifyStart(NotifyAppointmentStartCommand command);
}
