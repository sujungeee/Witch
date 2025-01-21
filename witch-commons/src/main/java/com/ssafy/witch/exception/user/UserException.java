package com.ssafy.witch.exception.user;

import com.ssafy.witch.exception.ErrorCode;

public class UserException extends RuntimeException {

  private final ErrorCode errorCode;

  public UserException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
