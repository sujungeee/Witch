package com.ssafy.witch.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;

import com.ssafy.witch.exception.user.UserEmailDuplicatedException;
import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.BDDMockito.BDDStubber;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// 1. 이메일 중복이 아닌지
// 2. 중복 아닌 경우 발급하고 저장 되는지

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class VerifyUserEmailServiceTest {

  @InjectMocks
  private VerifyUserEmailService sut;

  @Mock
  private ValidateUserUseCase validateUserUseCase;

  @Mock
  private EmailVerificationCodeGeneratorPort emailVerificationCodeGeneratorPort;
  @Mock
  private EmailVerificationCodeCachePort emailVerificationCodeCachePort;
  @Mock
  private VerifiedEmailCachePort verifiedEmailCachePort;
  @Mock
  private EmailVerificationGeneratedEventPort emailVerificationGeneratedEventPort;

  @Test
  void 이메일_인증_코드_발급시_이메일이_중복이면_발급할_수_없다() {
    //given
    String email = "test@test.com";
    BDDMockito
        .willThrow(new UserEmailDuplicatedException())
        .given(validateUserUseCase).checkUserEmailDuplication(email);
    //when
    //then
    Assertions.assertThatThrownBy(() -> {
      CreateUserEmailVerificationCodeCommand command = new CreateUserEmailVerificationCodeCommand(email);
      sut.createUserEmailVerificationCode(command);
    }).isInstanceOf(UserEmailDuplicatedException.class);
  }

  @Test
  void 이메일_중복이_아니면_코드_발급이_발생해야한다() {
    //given
    String email = "test@test.com";
    EmailVerificationCode fakeCode = EmailVerificationCode.of("123456");

    BDDMockito
        .given(emailVerificationCodeGeneratorPort.generate(email))
        .willReturn(fakeCode);

    //when & then
    Assertions.assertThatCode(()->{
      CreateUserEmailVerificationCodeCommand command = new CreateUserEmailVerificationCodeCommand(email);
      sut.createUserEmailVerificationCode(command);
    }).doesNotThrowAnyException();

    //이벤트 정상 발행 (publish인자 EmailVerificationCodeGeneratedEvent.of(email, fakeCode)로 넘기면 안 됨 )
    BDDMockito.then(emailVerificationGeneratedEventPort).should(only()).publish(any());
    BDDMockito.then(emailVerificationCodeCachePort).should().upsert(any(), any(), any());
  }
}