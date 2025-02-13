package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;

public interface NotifyAppointmentUseCase {

  void notifyJoin(NotifyAppointmentJoinCommand command);

}
