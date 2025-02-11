package com.ssafy.witch.controller.group;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.GroupNotJoinedException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.DeleteGroupUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;

@WebMvcTest(GroupDeleteController.class)
class GroupDeleteControllerTest extends RestDocsTestSupport {

    @MockBean
    private DeleteGroupUseCase deleteGroupUseCase;

    @WithMockWitchUser
    @Test
    void delete_group_200() throws Exception {

        String groupId = "groupId-id-example";

        mvc.perform(delete("/groups/{groupId}", groupId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("groupId").description("삭제하려는 모임 id")
                        ),
                requestHeaders(headerWithName(
                        "Authorization")
                            .description("JWT access token")
                ),
                        responseFields
                                (fieldWithPath("success")
                                        .type(BOOLEAN)
                                        .description("성공 여부")
                        )
                ));
    }


    @WithMockWitchUser
    @Test
    void delete_group_400_group_not_exists() throws Exception {
        String groupId = "group-id-example";

        doThrow(new GroupNotFoundException())
                .when(deleteGroupUseCase).deleteGroup(any());

        mvc.perform(delete("/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("error.errorCode")
                                .value(ErrorCode.GROUP_NOT_EXIST.getErrorCode()))
                .andDo(restDocs.document());
    }

    @WithMockWitchUser
    @Test
    void delete_group_400_not_group_member() throws Exception {
        String groupId = "group-id-example";

        doThrow(new GroupNotJoinedException())
                .when(deleteGroupUseCase).deleteGroup(any());

        mvc.perform(delete("/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("error.errorCode")
                                .value(ErrorCode.NOT_JOINED_MEETING.getErrorCode()))
                .andDo(restDocs.document());
    }

    @WithMockWitchUser
    @Test
    void delete_group_400_user_is_not_leader() throws Exception {
        String groupId = "group-id-example";

        doThrow(new UnauthorizedGroupAccessException())
                .when(deleteGroupUseCase).deleteGroup(any());

        mvc.perform(delete("/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer sample.access.token")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("error.errorCode")
                                .value(ErrorCode.UNAUTHORIZED_GROUP_ACCESS.getErrorCode()))
                .andDo(restDocs.document());
    }

}