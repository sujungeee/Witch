package com.ssafy.witch.controller.appointment.mapper;

import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.controller.appointment.response.AppointmentListResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentResponseMapper {

  AppointmentListResponse toResponse(AppointmentListOutput output);
}
