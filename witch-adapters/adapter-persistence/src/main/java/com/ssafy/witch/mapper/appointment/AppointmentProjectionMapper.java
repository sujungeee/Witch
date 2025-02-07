package com.ssafy.witch.mapper.appointment;

import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.entity.appointment.AppointmentEntityProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentProjectionMapper {

  AppointmentProjection toProjection(AppointmentEntityProjection appointmentEntityProjection);

}
