package com.ssafy.witch.user;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

  private String userId;

  private String email;

  private String password;

  private String nickname;

  private String profileImageUrl;

  public User(String userId, String email, String password, String nickname,
      String profileImageUrl) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }

  public static User createNewUser(String email, String password, String nickname) {
    return new User(
        UUID.randomUUID().toString(),
        email,
        password,
        nickname,
        null
    );
  }

}
