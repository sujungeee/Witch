package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotifyAppointmentService implements NotifyAppointmentUseCase {

  private final NotifyAppointmentPort notifyAppointmentPort;

  @Override
  public void notifyJoin(NotifyAppointmentJoinCommand command) {

    AppointmentJoinNotification notification = command.toNotification(command);
    notifyAppointmentPort.notifyJoinAppointment(notification);
  }
}
