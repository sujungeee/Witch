package com.ssafy.witch.exception.auth;

import com.ssafy.witch.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtAuthException extends RuntimeException {

  private final ErrorCode errorCode;

  public JwtAuthException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
