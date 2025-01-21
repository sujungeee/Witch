package com.ssafy.witch.controller;

import com.ssafy.witch.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WitchApiResponse<T> {

  private final boolean success;
  private final T data;

  private WitchApiResponse(boolean success, T data) {
    this.success = success;
    this.data = data;
  }

  public static <T> WitchApiResponse<T> success(T data) {
    return new WitchApiResponse<>(true, data);
  }

  public static  WitchApiResponse<WitchError> failure(ErrorCode errorCode) {
    return new WitchApiResponse<>(false, WitchError.of(errorCode));
  }

}
