package com.ssafy.witch.event;

public class AppointmentEvent {

  public final static String JOIN_APPOINTMENT = "APPOINTMENT_JOIN";
  public static final String START_APPOINTMENT = "APPOINTMENT_START";
  public static final String END_APPOINTMENT = "APPOINTMENT_END";
  public static final String ARRIVAL_APPOINTMENT = "APPOINTMENT_ARRIVAL";

  private AppointmentEvent() {
  }
}
