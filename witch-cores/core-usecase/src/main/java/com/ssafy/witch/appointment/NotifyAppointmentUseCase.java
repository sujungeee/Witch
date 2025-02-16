package com.ssafy.witch.appointment;

import com.ssafy.witch.appointment.command.NotifyAppointmentArrivalCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentEndCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentStartCommand;

public interface NotifyAppointmentUseCase {

  void notifyJoin(NotifyAppointmentJoinCommand command);

  void notifyStart(NotifyAppointmentStartCommand command);

  void notifyEnd(NotifyAppointmentEndCommand command);

  void notifyArrival(NotifyAppointmentArrivalCommand command);
}
