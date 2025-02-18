package com.ssafy.witch.appointment;

public interface AppointmentMemberPositionCachePort {


  void upsert(String userId, Position position);

  Position get(String userId);
}