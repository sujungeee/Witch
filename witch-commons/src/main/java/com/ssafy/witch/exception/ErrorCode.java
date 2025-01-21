package com.ssafy.witch.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  INTERNAL_SERVER_ERROR("WCH0000", "서버에 문제가 발생했습니다."),
  ;

  private final String errorCode;
  private final String errorMessage;

  ErrorCode(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "[" + errorCode + "] " + errorMessage;
  }
}
