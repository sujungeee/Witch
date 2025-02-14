package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPositionCachePort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.appointment.command.UpdateAppointmentMemberPositionCommand;
import com.ssafy.witch.exception.appointment.AppointmentNotOnGoingException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateAppointmentMemberPositionService implements
    UpdateAppointmentMemberPositionUseCase {

  private final OnGoingAppointmentCachePort onGoingAppointmentCachePort;
  private final AppointmentMemberPositionCachePort appointmentMemberPositionCachePort;

  @Override
  public void execute(UpdateAppointmentMemberPositionCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();
    double latitude = command.getLatitude();
    double longitude = command.getLongitude();

    if (!onGoingAppointmentCachePort.has(appointmentId)) {
      throw new AppointmentNotOnGoingException();
    }

    AppointmentDetailProjection appointmentDetailProjection = onGoingAppointmentCachePort.get(
        appointmentId);

    appointmentDetailProjection.getMembers().stream()
        .filter(member -> member.getUserId().equals(userId))
        .findFirst().orElseThrow(UnauthorizedAppointmentAccessException::new);

    appointmentMemberPositionCachePort.upsert(userId, new Position(latitude, longitude));
  }
}
