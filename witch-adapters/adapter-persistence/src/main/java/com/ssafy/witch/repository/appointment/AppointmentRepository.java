package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.mapper.appointment.AppointmentMapper;
import java.time.LocalDateTime;
import java.util.Optional;
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
  public boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime) {
    return appointmentJpaRepository.existsConflictAppointment(userId, appointmentTime);
  }

  @Override
  public Optional<Appointment> findById(String appointmentId) {
    return appointmentJpaRepository.findById(appointmentId).map(appointmentMapper::toDomain);
  }

}
