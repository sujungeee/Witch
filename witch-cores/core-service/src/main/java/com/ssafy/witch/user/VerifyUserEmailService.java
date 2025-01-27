package com.ssafy.witch.user;

import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VerifyUserEmailService implements VerifyUserEmailUseCase {

  private final ValidateUserUseCase validateUserUseCase;

  private final EmailVerificationCodeGeneratorPort emailVerificationCodeGeneratorPort;
  private final EmailVerificationCodeStorePort emailVerificationCodeStorePort;
  private final EmailVerificationGeneratedEventPort emailVerificationGeneratedEventPort;


  @Value("${witch.email.verification-code.expire-minute}")
  private int emailVerificationCodeExpireMinute;

  @Transactional
  @Override
  public void createUserEmailVerificationCode(CreateUserEmailVerificationCodeCommand command) {

    String email = command.getEmail();
    validateUserUseCase.checkUserEmailDuplication(email);

    EmailVerificationCode code = emailVerificationCodeGeneratorPort.generate(email);

    emailVerificationGeneratedEventPort.publish(
        EmailVerificationCodeGeneratedEvent.of(email, code));

    emailVerificationCodeStorePort.upsert(email, code,
        Duration.ofMinutes(emailVerificationCodeExpireMinute));
  }
}
