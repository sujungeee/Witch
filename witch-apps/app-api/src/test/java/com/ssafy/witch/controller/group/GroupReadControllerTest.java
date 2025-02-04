package com.ssafy.witch.controller.group;

import static com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.group.mapper.GroupResponseMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupReadUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({GroupReadController.class, GroupResponseMapper.class})
class GroupReadControllerTest extends RestDocsTestSupport {

  @MockBean
  private GroupReadUseCase groupReadUseCase;

  @WithMockWitchUser
  @Test
  void get_group_preview_200() throws Exception {

    Group group = new Group(
        "group-id-example",
        "구미푸파",
        "http://group.image.example"
    );

    given(groupReadUseCase.getGroupPreview(any())).willReturn(group);

    mvc.perform(get("/groups/{groupId}/preview", group.getGroupId())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("조회하고자 하는 모임 식별자")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.groupId")
                    .type(STRING)
                    .description("모임 식별키"),
                fieldWithPath("data.name")
                    .type(STRING)
                    .description("모임 이름"),
                fieldWithPath("data.groupImageUrl")
                    .type(STRING)
                    .description("모임 이미지 URL")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void get_group_preview_400_not_exist_group_id() throws Exception {

    Group group = new Group(
        "group-id-example",
        "구미푸파",
        "http://group.image.example"
    );

    given(groupReadUseCase.getGroupPreview(any()))
        .willThrow(new GroupNotFoundException());

    mvc.perform(get("/groups/{groupId}/preview", group.getGroupId())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }
}
