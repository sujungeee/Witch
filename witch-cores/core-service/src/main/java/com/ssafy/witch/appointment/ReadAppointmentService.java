package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentReadPort;
import com.ssafy.witch.appointment.mapper.AppointmentOutputMapper;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.GroupMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadAppointmentService implements ReadAppointmentUseCase {

  private final AppointmentReadPort appointmentReadPort;
  private final GroupMemberPort groupMemberPort;

  private final AppointmentOutputMapper appointmentOutputMapper;

  @Override
  public AppointmentListOutput getAppointments(String userId, String groupId) {
    verifyUserInGroup(userId, groupId);
    return appointmentOutputMapper.toOutput(appointmentReadPort.getAppointments(userId, groupId));
  }


  private void verifyUserInGroup(String userId, String groupId) {
    if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
      throw new UnauthorizedGroupAccessException();
    }
  }
}
