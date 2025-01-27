package com.ssafy.witch.controller.user;

import static com.ssafy.witch.support.docs.RestDocsUtils.constraints;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.UserEmailVerificationCodeRequest;
import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.support.docs.RestDocsTestSupport;
import com.ssafy.witch.user.VerifyUserEmailUseCase;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({UserEmailVerificationController.class, UserRequestMapper.class})
class UserEmailVerificationControllerTest extends RestDocsTestSupport {

  @MockBean
  private VerifyUserEmailUseCase verifyUserEmailUseCase;

  @Test
  void generate_user_email_verification_code_200() throws Exception {

    String email = "test@test.com";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andDo(restDocs.document(
            requestFields(
                fieldWithPath("email")
                    .type(STRING)
                    .description("인증을 요청할 사용자 이메일")
                    .attributes(constraints("이메일 형식"))
            ),
            responseFields(
                fieldWithPath("success")
                    .type(BOOLEAN)
                    .description("성공 여부")
            )
        ));
  }

  @Test
  void generate_user_email_verification_code_400_invalid_email_format() throws Exception {

    String email = "testtestcom";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    doThrow(new ConstraintViolationException(null))
        .when(verifyUserEmailUseCase).createUserEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }

  @Test
  void generate_user_email_verification_code_400_duplicated_email() throws Exception {

    String email = "test@test.com";
    UserEmailVerificationCodeRequest request =
        new UserEmailVerificationCodeRequest(email);

    doThrow(new UserEmailDuplicatedException())
        .when(verifyUserEmailUseCase).createUserEmailVerificationCode(any());

    mvc.perform(post("/users/email-verification-code")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andDo(restDocs.document());
  }
}
