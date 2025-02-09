package com.ssafy.witch.controller.appointment.response;

import com.ssafy.witch.appointment.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppointmentDetailResponse {

  private String appointmentId;
  private String name;
  private AppointmentStatus appointmentStatus;
  private String summary;
  private LocalDateTime appointmentTime;
  private String address;
  private Double longitude;
  private Double latitude;

  private List<AppointmentMemberResponse> members;

}
