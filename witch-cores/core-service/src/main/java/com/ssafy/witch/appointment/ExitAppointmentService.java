package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.command.AppointmentExitCommand;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExitAppointmentService implements ExitAppointmentUseCase {

  private final AppointmentPort appointmentPort;
  private final AppointmentMemberPort appointmentMemberPort;

  @Transactional
  @Override
  public void exitAppointment(AppointmentExitCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();

    validateAppointmentExists(appointmentId);

    AppointmentMember appointmentMember =
        appointmentMemberPort.findByUserIdAndAppointmentId(userId, appointmentId)
            .orElseThrow(NotJoinedAppointmentException::new);

    validateUserNotLeader(appointmentMember);

    appointmentMemberPort.delete(appointmentMember);
  }

  private void validateAppointmentExists(String appointmentId) {
    if (!appointmentPort.existsById(appointmentId)) {
      throw new AppointmentNotFoundException();
    }
  }

  private void validateUserNotLeader(AppointmentMember appointmentMember) {
    if (appointmentMember.getIsLeader()) {
      throw new UnauthorizedAppointmentAccessException();
    }
  }
}
