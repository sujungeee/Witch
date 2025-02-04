package com.ssafy.witch.controller.group;

import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.AlreadyJoinedGroupException;
import com.ssafy.witch.exception.group.GroupJoinRequestExistsException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.group.CreateGroupJoinRequestUseCase;
import com.ssafy.witch.group.HandleGroupJoinRequestUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(GroupJoinController.class)
class GroupJoinControllerTest extends RestDocsTestSupport {

  @MockBean
  public CreateGroupJoinRequestUseCase createGroupJoinRequestUseCase;

  @MockBean
  private HandleGroupJoinRequestUseCase handleGroupJoinRequestUseCase;

  @WithMockWitchUser
  @Test
  void create_group_join_request_200() throws Exception {

    String groupId = "sample-group-id";

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            pathParameters(
                parameterWithName("groupId")
                    .description("가입을 요청하고자 하는 모임 식별자")
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void create_group_join_request_400_group_not_exists() throws Exception {

    String groupId = "sample-group-id";

    doThrow(new GroupNotFoundException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_group_join_request_400_already_joined_group() throws Exception {
    String groupId = "sample-group-id";

    doThrow(new AlreadyJoinedGroupException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(ErrorCode.ALREADY_JOINED_GROUP.getErrorCode()))
        .andDo(restDocs.document());
  }

  @WithMockWitchUser
  @Test
  void create_group_join_request_400_request_already_exists() throws Exception {
    String groupId = "sample-group-id";

    doThrow(new GroupJoinRequestExistsException())
        .when(createGroupJoinRequestUseCase).creatGroupJoinRequest(any());

    mvc.perform(post("/groups/{groupId}/join-requests", groupId)
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("error.errorCode").value(
            ErrorCode.GROUP_JOIN_REQUEST_ALREADY_EXISTS.getErrorCode()))
        .andDo(restDocs.document());
  }

}
