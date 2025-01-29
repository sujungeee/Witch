package com.ssafy.witch.jwt.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import util.JwtConst;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponse {

  private final String tokenType = JwtConst.TOKEN_TYPE;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String accessToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long accessTokenExpiresIn;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String refreshToken;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long refreshTokenExpiresIn;


  public static TokenResponse refresh(String accessToken,
      Long accessTokenExpiresIn) {
    return new TokenResponse(accessToken, accessTokenExpiresIn, null, null);
  }

  public static TokenResponse create(String accessToken,
      Long accessTokenExpiresIn,
      String refreshToken, Long refreshTokenExpiresIn) {
    return new TokenResponse(accessToken, accessTokenExpiresIn, refreshToken,
        refreshTokenExpiresIn);
  }
}
