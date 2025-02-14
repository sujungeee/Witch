package com.ssafy.witch.controller.user.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserPasswordChangeRequest {

  private String password;
  private String newPassword;

}
