package com.ssafy.witch.controller.appointment;

import com.ssafy.witch.appointment.ReadAppointmentUseCase;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.controller.appointment.response.AppointmentListResponse;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class AppointmentReadController {

  private final ReadAppointmentUseCase readAppointmentUseCase;
  private final AppointmentResponseMapper appointmentResponseMapper;

  @GetMapping("/groups/{groupId}/appointments")
  public WitchApiResponse<AppointmentListResponse> getAppointments(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {
    AppointmentListOutput output = readAppointmentUseCase.getAppointments(userId, groupId);
    AppointmentListResponse response = appointmentResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }
}
