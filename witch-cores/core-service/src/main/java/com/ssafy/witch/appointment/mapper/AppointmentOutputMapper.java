package com.ssafy.witch.appointment.mapper;

import com.ssafy.witch.apoointment.model.AppointmentProjection;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentOutputMapper {

  default AppointmentListOutput toOutput(List<AppointmentProjection> projections) {
    return new AppointmentListOutput(appointmentProjectionToOutputList(projections));
  }

  List<AppointmentOutput> appointmentProjectionToOutputList(
      List<AppointmentProjection> projections);
}
