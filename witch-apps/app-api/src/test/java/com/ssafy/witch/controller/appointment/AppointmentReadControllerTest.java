package com.ssafy.witch.controller.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.appointment.ReadAppointmentUseCase;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({AppointmentReadController.class, AppointmentResponseMapper.class})
class AppointmentReadControllerTest extends RestDocsTestSupport {

  @MockBean
  private ReadAppointmentUseCase readAppointmentUseCase;

  @WithMockWitchUser
  @Test
  void get_appointments_200() throws Exception {

    String groupId = "example-group-id";

    AppointmentListOutput output = new AppointmentListOutput(
        List.of(
            new AppointmentOutput("example-appontment-id-1",
                "약속 이름 1",
                LocalDateTime.of(2030, 1, 1, 20, 0, 10),
                AppointmentStatus.ONGOING, true),
            new AppointmentOutput("example-appontment-id-2",
                "약속 이름 2",
                LocalDateTime.of(2030, 1, 1, 23, 0, 10),
                AppointmentStatus.SCHEDULED, false)
        )
    );

    given(readAppointmentUseCase.getAppointments(any(), any())).willReturn(output);

    mvc.perform(get("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("조회하고자 하는 모임 식별자")
            ),
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.appointments.[].appointmentId")
                    .type(STRING)
                    .description("약속 식별자"),
                fieldWithPath("data.appointments.[].name")
                    .type(STRING)
                    .description("약속 이름"),
                fieldWithPath("data.appointments.[].appointmentTime")
                    .type(STRING)
                    .description("약속 시간"),
                fieldWithPath("data.appointments.[].status")
                    .type(STRING)
                    .description("약속 상태(SCHEDULED, ONGOING, FINISHED"),
                fieldWithPath("data.appointments.[].isMyAppointment")
                    .type(BOOLEAN)
                    .description("조회자 약속 참여 여부")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_appointments_400_user_not_in_group() throws Exception {

    String groupId = "example-group-id";

    given(readAppointmentUseCase.getAppointments(any(), any()))
        .willThrow(new UnauthorizedGroupAccessException());

    mvc.perform(get("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

}
