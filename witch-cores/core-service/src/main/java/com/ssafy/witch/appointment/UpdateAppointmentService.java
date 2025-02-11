package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentPort;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentService implements UpdateAppointmentUseCase {

  private final AppointmentPort appointmentPort;

  @Transactional
  public List<Appointment> startAppointments(LocalDateTime appointmentTime) {

    List<Appointment> appointments =
        appointmentPort.findAllByAppointmentTime(appointmentTime);

    appointments.forEach(Appointment::startScheduledAppointment);

    appointmentPort.saveAll(appointments);

    return appointments;
  }

  @Override
  public List<Appointment> endAppointments(LocalDateTime now) {

    List<Appointment> appointments =
        appointmentPort.findAllByAppointmentTime(now);

    appointments.forEach(Appointment::endOngoingAppointment);

    appointmentPort.saveAll(appointments);

    return appointments;
  }
}
