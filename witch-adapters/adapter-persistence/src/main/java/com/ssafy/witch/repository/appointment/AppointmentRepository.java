package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.mapper.appointment.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentRepository implements AppointmentPort {

  private final AppointmentJpaRepository appointmentJpaRepository;
  private final AppointmentMapper appointmentMapper;

  @Override
  public Appointment save(Appointment appointment) {
    return appointmentMapper.toDomain(
        appointmentJpaRepository.save(appointmentMapper.toEntity(appointment))
    );
  }

  @Override
  public boolean hasOngoingAppointment(String userId) {
    return appointmentJpaRepository.existsOngoingAppointmentByUserId(userId);
  }
}
