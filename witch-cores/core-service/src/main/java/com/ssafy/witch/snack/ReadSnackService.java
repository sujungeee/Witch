package com.ssafy.witch.snack;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.snack.mapper.SnackOutputMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReadSnackService implements ReadSnackUseCase {

  private final SnackPort snackPort;
  private final AppointmentMemberPort appointmentMemberPort;
//  private final SnackOutputMapper snackOutputMapper;

  @Override
  public Snack getSnackDetail(String userId, String snackId){

    Snack snack = snackPort.findById(snackId).orElseThrow(SnackNotFoundException::new);

    if (!appointmentMemberPort.existsByUserIdAndAppointmentId(userId, snack.getAppointmentId())) {
      throw new UnauthorizedAppointmentAccessException();
    }

    return snack;
  }
}
