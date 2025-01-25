package com.ssafy.witch.user;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class WitchUser {

  private final String userId;

  private final String email;

  private final String password;

  private final String nickname;

  private final String profileImageUrl;

  public WitchUser(String userId, String email, String password, String nickname,
      String profileImageUrl) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }

  private static WitchUser createNewUser(String email, String password, String nickname) {
    return new WitchUser(
        UUID.randomUUID().toString(),
        email,
        password,
        nickname,
        null
    );
  }

}
