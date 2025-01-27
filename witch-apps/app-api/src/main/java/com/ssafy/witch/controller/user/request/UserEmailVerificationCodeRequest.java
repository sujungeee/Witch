package com.ssafy.witch.controller.user.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserEmailVerificationCodeRequest {

  private String email;

}
