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
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.snack.ReadSnackUseCase;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
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

    Snack snack = new Snack(
        "test-snack-id",
        "test-appointment-id",
        "test-user-id",
        50.21,
        48.23,
        "http://test.image.url",
        "http://test.sound.url",
        LocalDateTime.parse("2025-02-14T14:21:24")
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
                fieldWithPath("data.userId")
                    .type(STRING)
                    .description("스낵을 생성한 사용자 식별자"),
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
                    .description("스낵 생성 시간")
            )
        ));
  }

}