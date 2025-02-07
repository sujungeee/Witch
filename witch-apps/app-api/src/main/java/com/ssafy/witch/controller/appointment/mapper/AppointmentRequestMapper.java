package com.ssafy.witch.controller.appointment.mapper;

import com.ssafy.witch.appointment.command.AppointmentCreateCommand;
import com.ssafy.witch.controller.appointment.request.AppointmentCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentRequestMapper {

  AppointmentCreateCommand toCommand(
      String userId, String groupId,
      AppointmentCreateRequest request);
}
