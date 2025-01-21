package com.ssafy.witch.controller;

import com.ssafy.witch.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WitchError {
  private final String errorCode;
  private final String errorMessage;

  private WitchError(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
  public static WitchError of(ErrorCode errorCode) {
    return new WitchError(errorCode.getErrorCode(), errorCode.getErrorMessage());
  }
}
