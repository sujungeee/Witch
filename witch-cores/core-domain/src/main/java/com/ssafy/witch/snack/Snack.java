package com.ssafy.witch.snack;

import java.util.UUID;
import lombok.Getter;

@Getter
public class Snack {

  private String snackId;
  private String appointmentId;
  private String userId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;

  public Snack(String snackId, String appointmentId, String userId, Double longitude,
      Double latitude, String snackImageUrl, String snackSoundUrl) {
    this.snackId = snackId;
    this.appointmentId = appointmentId;
    this.userId = userId;
    this.longitude = longitude;
    this.latitude = latitude;
    this.snackImageUrl = snackImageUrl;
    this.snackSoundUrl = snackSoundUrl;
  }

  public static Snack createNewSnack(String appointmentId, String userId, Double longitude, Double latitude, String snackImageUrl, String snackSoundUrl) {
    return new Snack(
        UUID.randomUUID().toString(),
        appointmentId,
        userId,
        longitude,
        latitude,
        snackImageUrl,
        snackSoundUrl
    );
  }
}
