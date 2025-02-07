package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.appointment.AppointmentMember;
import com.ssafy.witch.mapper.appointment.AppointmentMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentMemberRepository implements AppointmentMemberPort {

  private final AppointmentMemberJpaRepository appointmentMemberJpaRepository;
  private final AppointmentMemberMapper appointmentMemberMapper;

  @Override
  public AppointmentMember save(AppointmentMember appointmentMember) {
    return appointmentMemberMapper.toDomain(
        appointmentMemberJpaRepository.save(appointmentMemberMapper.toEntity(appointmentMember)));
  }
}
