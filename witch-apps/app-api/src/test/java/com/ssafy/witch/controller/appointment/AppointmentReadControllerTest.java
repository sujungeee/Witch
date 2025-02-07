package com.ssafy.witch.controller.appointment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.appointment.ReadAppointmentUseCase;
import com.ssafy.witch.appointment.output.AppointmentListOutput;
import com.ssafy.witch.appointment.output.AppointmentOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupListOutput;
import com.ssafy.witch.appointment.output.AppointmentWithGroupOutput;
import com.ssafy.witch.controller.appointment.mapper.AppointmentResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.output.GroupOutput;
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


  @WithMockWitchUser
  @Test
  void get_appointments_400_group_not_exists() throws Exception {

    String groupId = "example-group-id";

    given(readAppointmentUseCase.getAppointments(any(), any()))
        .willThrow(new GroupNotFoundException());

    mvc.perform(get("/groups/{groupId}/appointments", groupId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }


  @WithMockWitchUser
  @Test
  void get_my_appointments_200() throws Exception {

    int year = 2030;
    int month = 1;

    AppointmentWithGroupListOutput output = new AppointmentWithGroupListOutput(
        List.of(
            new AppointmentWithGroupOutput("example-appontment-id-1",
                "약속 이름 1",
                LocalDateTime.of(2030, 1, 1, 20, 0, 10),
                AppointmentStatus.SCHEDULED,
                new GroupOutput("example-group-id-1", "그룹이름1", "http://example.group.image1")),
            new AppointmentWithGroupOutput("example-appontment-id-1",
                "약속 이름 1",
                LocalDateTime.of(2030, 1, 2, 20, 0, 10),
                AppointmentStatus.SCHEDULED,
                new GroupOutput("example-group-id-1", "그룹이름2", "http://example.group.image2"))
        )
    );

    given(readAppointmentUseCase.getMyAppointments(any(), anyInt(), anyInt())).willReturn(output);

    mvc.perform(get("/appointments/me")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .param("year", String.valueOf(year))
            .param("month", String.valueOf(month))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            queryParameters(
                parameterWithName("year")
                    .description("조회하고자 하는 연도"),
                parameterWithName("month")
                    .description("조회하고자 하는 월")
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
                fieldWithPath("data.appointments.[].group.groupId")
                    .type(STRING)
                    .description("모임 식별자"),
                fieldWithPath("data.appointments.[].group.name")
                    .type(STRING)
                    .description("모임 이름"),
                fieldWithPath("data.appointments.[].group.groupImageUrl")
                    .type(STRING)
                    .description("모임 사진 URL")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_my_appointments_400_invalid_query_params() throws Exception {

    int year = 2000;
    int month = 13;

    mvc.perform(get("/appointments/me")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .param("year", String.valueOf(year))
            .param("month", String.valueOf(month))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(
            ErrorCode.REQUIRED_FIELD_MISSING_OR_INVALID.getErrorCode()))
        .andDo(restDocs.document());
  }
}
