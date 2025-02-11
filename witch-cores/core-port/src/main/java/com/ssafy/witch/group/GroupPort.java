package com.ssafy.witch.group;

import com.ssafy.witch.appointment.Appointment;

import java.util.Optional;

public interface GroupPort {

  Group save(Group group);

  Optional<Group> findById(String groupId);

  boolean existsById(String groupId);

  void delete(Group group);
}
