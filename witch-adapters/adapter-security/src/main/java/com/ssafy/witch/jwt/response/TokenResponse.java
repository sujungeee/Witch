package com.ssafy.witch.jwt.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String tokenType;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String accessToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long accessTokenExpiresIn;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String refreshToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long refreshTokenExpiresIn;


  public static TokenResponse refresh(String tokenType, String accessToken,
      Long accessTokenExpiresIn) {
    return new TokenResponse(tokenType, accessToken, accessTokenExpiresIn, null, null);
  }

  public static TokenResponse renew(String tokenType, String accessToken, Long accessTokenExpiresIn,
      String refreshToken, Long refreshTokenExpiresIn) {
    return new TokenResponse(tokenType, accessToken, accessTokenExpiresIn, refreshToken,
        refreshTokenExpiresIn);
  }
}
