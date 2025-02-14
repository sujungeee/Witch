package com.ssafy.witch.snack.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDetailOutput {

  private String snackId;
  private String appointmentId;
  private String userId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;

}
