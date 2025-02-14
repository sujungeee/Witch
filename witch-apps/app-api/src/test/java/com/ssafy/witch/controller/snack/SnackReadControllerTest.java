package com.ssafy.witch.controller.snack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.ssafy.witch.controller.snack.mapper.SnackResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.exception.snack.SnackViewNotAvailableException;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.snack.ReadSnackUseCase;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.output.UserBasicOutput;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({SnackReadController.class, SnackResponseMapper.class})
class SnackReadControllerTest extends RestDocsTestSupport {

  @MockBean
  private ReadSnackUseCase readSnackUseCase;

  @WithMockWitchUser
  @Test
  void get_snack_detail_200() throws Exception {

    SnackDetailOutput snack = new SnackDetailOutput(
        "test-snack-id",
        "test-appointment-id",
        50.21,
        48.23,
        "http://test.image.url",
        "http://test.sound.url",
        LocalDateTime.parse("2025-02-14T14:21:24"),
        new UserBasicOutput(
            "test-user-id",
            "닉네임",
            "http://test.profile.url"
        )
    );

    given(readSnackUseCase.getSnackDetail(any(), any())).willReturn(snack);

    mvc.perform(get("/snacks/{snackId}", snack.getSnackId())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer sample.access.token")
    )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            pathParameters(
                parameterWithName("snackId")
                    .description("조회하고자 하는 스낵 식별자")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.snackId")
                    .type(STRING)
                    .description("스낵 식별자"),
                fieldWithPath("data.appointmentId")
                    .type(STRING)
                    .description("스낵이 생성된 약속 식별자"),
                fieldWithPath("data.longitude")
                    .type(NUMBER)
                    .description("스낵 경도"),
                fieldWithPath("data.latitude")
                    .type(NUMBER)
                    .description("스낵 위도"),
                fieldWithPath("data.snackImageUrl")
                  .type(STRING)
                    .description("스낵 이미지 URL"),
                fieldWithPath("data.snackSoundUrl")
                  .type(STRING)
                    .description("스낵 음성 URL"),
                fieldWithPath("data.createdAt")
                    .type(STRING)
                    .description("스낵 생성 시간"),
                fieldWithPath("data.user.userId")
                    .type(STRING)
                    .description("스낵 생성 사용자 식별자"),
                fieldWithPath("data.user.nickname")
                    .type(STRING)
                    .description("스낵 생성 사용자 닉네임"),
                fieldWithPath("data.user.profileImageUrl")
                    .type(STRING)
                    .description("스낵 생성 사용자 프로필 URL")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_snack_detail_400_not_exist_snack() throws Exception {

    String snackId = "test-snack-id";

    given(readSnackUseCase.getSnackDetail(any(), any()))
        .willThrow(new SnackNotFoundException());

    mvc.perform(get("/snacks/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.NON_EXISTENT_SNACK.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_snack_detail_400_not_appointment_member() throws Exception {

    String snackId = "test-snack-id";

    given(readSnackUseCase.getSnackDetail(any(), any()))
        .willThrow(new UnauthorizedAppointmentAccessException());

    mvc.perform(get("/snacks/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.UNAUTHORIZED_APPOINTMENT_ACCESS.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_snack_detail_400_not_ongoing_appointment() throws Exception {

    String snackId = "test-snack-id";

    given(readSnackUseCase.getSnackDetail(any(), any()))
        .willThrow(new NotOngoingAppointmentException());

    mvc.perform(get("/snacks/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.APPOINTMENT_NOT_ONGOING.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void get_snack_detail_400_view_not_available() throws Exception {

    String snackId = "test-snack-id";

    given(readSnackUseCase.getSnackDetail(any(), any()))
        .willThrow(new SnackViewNotAvailableException());

    mvc.perform(get("/snacks/{snackId}", snackId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(
                ErrorCode.SNACK_VIEW_NOT_AVAILABLE.getErrorCode()))
        .andDo(restDocs.document());
  }
}