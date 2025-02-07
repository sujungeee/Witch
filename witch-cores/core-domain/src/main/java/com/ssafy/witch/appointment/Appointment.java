package com.ssafy.witch.appointment;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Appointment {

  private String appointmentId;
  private String groupId;
  private String name;
  private String summary;
  private LocalDateTime appointmentTime;
  private AppointmentStatus status;
  private Double longitude;
  private Double latitude;
  private String address;

  public Appointment(String appointmentId, String groupId, String name, String summary,
      LocalDateTime appointmentTime, AppointmentStatus status, Double longitude, Double latitude,
      String address) {
    this.appointmentId = appointmentId;
    this.groupId = groupId;
    this.name = name;
    this.summary = summary;
    this.appointmentTime = appointmentTime;
    this.status = status;
    this.longitude = longitude;
    this.latitude = latitude;
    this.address = address;
  }

  public static Appointment createNewAppointment(String groupId, String name, String summary,
      LocalDateTime appointmentTime, AppointmentStatus status, Double longitude, Double latitude,
      String address) {
    return new Appointment(
        UUID.randomUUID().toString(),
        groupId,
        name,
        summary,
        appointmentTime,
        AppointmentStatus.SCHEDULED,
        longitude,
        latitude,
        address
    );
  }
}
