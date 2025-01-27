package com.ssafy.witch.user;

import java.time.Duration;

public interface EmailVerificationCodeStorePort {

  void upsert(String email, EmailVerificationCode code, Duration expireDuration);

  EmailVerificationCode get(String email);

  void remove(String email);

  boolean has(String email);
}
