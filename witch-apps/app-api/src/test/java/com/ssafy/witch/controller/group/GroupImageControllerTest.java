package com.ssafy.witch.controller.group;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.file.UnsupportedFileFormatException;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.group.GroupImageUseCase;
import com.ssafy.witch.mock.user.WithMockWitchUser;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest({GroupImageController.class,
    FileRequestMapper.class, FileResponseMapper.class})
class GroupImageControllerTest extends RestDocsTestSupport {

  @MockBean
  private GroupImageUseCase groupImageUseCase;

  @WithMockWitchUser
  @Test
  void generate_group_image_presigned_url_200() throws Exception {

    PresignedUrlRequest request = new PresignedUrlRequest("group-image.jpeg");

    String presignedUrl = "http://presigned.url.example";
    String objectKey = "/object/key/example";

    BDDMockito.given(groupImageUseCase.generateGroupImagePresignedUrl(any()))
        .willReturn(new PresignedUrl(presignedUrl, objectKey));

    mvc.perform(post("/groups/group-image/presigned-url")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestHeaders(
                headerWithName("Authorization")
                    .description("JWT access token")
            ),
            requestFields(
                fieldWithPath("fileName")
                    .type(STRING)
                    .description("업로드할 파일명")
                    .attributes(constraints("확장자가 jpg, jpeg 또는 png여야 합니다"))
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부"),
                fieldWithPath("data.presignedUrl")
                    .type(STRING)
                    .description("PUT으로 해당 URL에 파일 업로드"),
                fieldWithPath("data.objectKey")
                    .type(STRING)
                    .description("업로드될 파일의 object key")
            )
        ));
  }

  @WithMockWitchUser
  @Test
  void generate_group_image_presigned_url_400_unsupported_file_format() throws Exception {

    PresignedUrlRequest request = new PresignedUrlRequest("group-image.mp3");

    BDDMockito.given(groupImageUseCase.generateGroupImagePresignedUrl(any()))
        .willThrow(new UnsupportedFileFormatException());

    mvc.perform(post("/groups/group-image/presigned-url")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer sample.access.token")
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("error.errorCode").value(ErrorCode.UNSUPPORTED_FILE_FORMAT.getErrorCode()))
        .andDo(restDocs.document());
  }

}
