package com.ssafy.witch.snack;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.appointment.Appointment;
import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.snack.command.SnackCreateCommand;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateSnackService implements CreateSnackUseCase {

  private final AppointmentPort appointmentPort;
  private final AppointmentMemberPort appointmentMemberPort;
  private final SnackPort snackPort;

  private final FileOwnerCachePort fileOwnerCachePort;
  private final PresignedUrlPort presignedUrlPort;

  public void createSnack(SnackCreateCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();
    Double longitude = command.getLongitude();
    Double latitude = command.getLatitude();
    String snackImageObjectKey = command.getSnackImageObjectKey();
    String snackSoundObjectKey = command.getSnackSoundObjectKey();

    //존재하는 약속?
    validateAppointmentExists(appointmentId);

    //약속 상태가 ongoing?
    validateAppointmentStatus(appointmentId);

    //해당 약속의 멤버인지?
    validateAppointmentAuthorization(userId, appointmentId);

    //파일의 소유권이 있는지?
    validateImageOwnership(userId, snackImageObjectKey);

    if (Objects.nonNull(snackSoundObjectKey)) {
      validateSoundOwnership(userId, snackSoundObjectKey);
    }
    //presignedUrlPort.getAccessUrl
    String snackImageUrl = getImageAccessUrl(snackImageObjectKey);
    String snackSoundUrl = getSoundAccessUrl(snackSoundObjectKey);

    Snack newSnack = Snack.createNewSnack(appointmentId, userId, longitude, latitude, snackImageUrl,
        snackSoundUrl);

    snackPort.save(newSnack);
  }

  private String getSoundAccessUrl(String snackSoundObjectKey) {
    if (Objects.isNull(snackSoundObjectKey)) {
      return null;
    }

    return presignedUrlPort.getAccessUrl(snackSoundObjectKey);
  }

  private String getImageAccessUrl(String snackImageObjectKey) {
    return presignedUrlPort.getAccessUrl(snackImageObjectKey);
  }

  private void validateAppointmentAuthorization(String userId, String appointmentId) {
    if (!appointmentMemberPort.existsByUserIdAndAppointmentId(userId, appointmentId)) {
      throw new UnauthorizedAppointmentAccessException();
    }
  }

  private void validateAppointmentExists(String appointmentId) {
    if (!appointmentPort.existsById(appointmentId)) {
      throw new AppointmentNotFoundException();
    }
  }

  private void validateImageOwnership(String userId, String snackImageObjectKey) {

    String imageOwnerId = fileOwnerCachePort.getOwnerId(snackImageObjectKey);

    fileOwnerCachePort.delete(snackImageObjectKey);

    if (!userId.equals(imageOwnerId)) {
      throw new InvalidFileOwnerException();
    }
  }
  private void validateSoundOwnership(String userId, String snackSoundObjectKey) {

    String soundOwnerId = fileOwnerCachePort.getOwnerId(snackSoundObjectKey);

    fileOwnerCachePort.delete(snackSoundObjectKey);

    if (!userId.equals(soundOwnerId)) {
      throw new InvalidFileOwnerException();
    }
  }

  private void validateAppointmentStatus(String appointmentId) {
    Appointment appointment = appointmentPort.findById(appointmentId).get();
    if (!AppointmentStatus.ONGOING.equals(appointment.getStatus())) {
      throw new NotOngoingAppointmentException();
    }
  }

}
