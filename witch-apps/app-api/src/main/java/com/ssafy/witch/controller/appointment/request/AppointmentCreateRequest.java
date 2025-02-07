package com.ssafy.witch.controller.appointment.request;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AppointmentCreateRequest {

  private String name;
  private String summary;
  private LocalDateTime appointmentTime;
  private Double longitude;
  private Double latitude;
  private String address;

}
