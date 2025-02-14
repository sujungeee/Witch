package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.appointment.command.NotifyAppointmentStartCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotifyAppointmentService implements NotifyAppointmentUseCase {

  private final NotifyAppointmentPort notifyAppointmentPort;

  @Override
  public void notifyJoin(NotifyAppointmentJoinCommand command) {

    AppointmentJoinNotification notification = command.toNotification();
    notifyAppointmentPort.notifyJoinAppointment(notification);
  }

  @Override
  public void notifyStart(NotifyAppointmentStartCommand command) {
    AppointmentStartNotification notification = command.toNotification();
    notifyAppointmentPort.notifyStartAppointment(notification);
  }
}
