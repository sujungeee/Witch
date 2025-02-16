package com.ssafy.witch.user;


import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.AppointmentMember;
import com.ssafy.witch.appointment.DeleteAppointmentUseCase;
import com.ssafy.witch.appointment.ExitAppointmentUseCase;
import com.ssafy.witch.appointment.command.AppointmentDeleteCommand;
import com.ssafy.witch.appointment.command.AppointmentExitCommand;
import com.ssafy.witch.exception.user.IncorrectPasswordException;
import com.ssafy.witch.exception.user.UserNotFoundException;
import com.ssafy.witch.group.GroupMember;
import com.ssafy.witch.group.GroupMemberPort;
import com.ssafy.witch.group.GroupPort;
import com.ssafy.witch.group.LeaveGroupUseCase;
import com.ssafy.witch.group.command.LeaveGroupCommand;
import com.ssafy.witch.user.command.DeleteUserCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteUserService implements DeleteUserUseCase {

  private final UserPort userPort;
  private final GroupMemberPort groupMemberPort;
  private final AppointmentMemberPort appointmentMemberPort;

  private final PasswordEncoder passwordEncoder;
  private final LeaveGroupUseCase leaveGroupUseCase;
  private final DeleteAppointmentUseCase deleteAppointmentUseCase;
  private final ExitAppointmentUseCase exitAppointmentUseCase;


  @Transactional
  @Override
  public void delete(DeleteUserCommand command) {
    String userId = command.getUserId();
    String password = command.getPassword();
    String refreshToken = command.getRefreshToken();

    User user = userPort.findById(userId).orElseThrow(UserNotFoundException::new);

    //비밀 번호 일치하는지 확인
    validatePassword(user, password);

    //해당 사용자가 가입한 약속 확인
    List<AppointmentMember> appointmentMembers = appointmentMemberPort.findAllByUserId(userId);
    //해당 사용자가 가입한 모임 확인
    List<GroupMember> groupMembers = groupMemberPort.findAllByUserId(userId);

    for (AppointmentMember appointment: appointmentMembers) {
      //약속 리더인 경우 약속 삭제
      if (appointment.getIsLeader()) {
        deleteAppointmentUseCase.deleteAppointment(new AppointmentDeleteCommand(userId, appointment.getAppointmentId()));
      } else {
        //약속 리더 아닌 경우 나가기
        exitAppointmentUseCase.exitAppointment(new AppointmentExitCommand(userId, appointment.getAppointmentId()));
      }
    }

    for (GroupMember group : groupMembers) {
      //모임 리더인 경우 모임 삭제
      if (group.getIsLeader()) {
        // 성원이형 모임 삭제 서비스 사용

      } else {
        //모임 리더 아닌 경우 모임 나가기
        leaveGroupUseCase.leaveGroup(new LeaveGroupCommand(userId, group.getGroupId()));
      }
    }
    //2. 모임 없으면 약속도 없으므로 RefreshTokenRedisRepository.delete(user.getEmail)로 로그아웃

    //3. userPort.delete(user)로 탈퇴
    userPort.delete(user);
  }

  private void validatePassword(User user, String password) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IncorrectPasswordException();
    }
  }

}
