package com.ssafy.witch.controller.snack.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SnackDetailResponse {

  private String snackId;
  private String userId;
  private Double longitude;
  private Double latitude;
  private String snackImageUrl;
  private String snackSoundUrl;
  private LocalDateTime createdAt;
}
