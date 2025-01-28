package com.ssafy.witch.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.witch.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WitchApiResponse<T> {

  private final boolean success;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final T data;

  private WitchApiResponse(boolean success, T data) {
    this.success = success;
    this.data = data;
  }

  public static <T> WitchApiResponse<T> success(T data) {
    return new WitchApiResponse<>(true, data);
  }

  public static WitchApiResponse<Void> success() {
    return new WitchApiResponse<>(true, null);
  }

  public static WitchApiResponse<WitchError> failure(ErrorCode errorCode) {
    return new WitchApiResponse<>(false, WitchError.of(errorCode));
  }

  public static WitchApiResponse<WitchError> failure(ErrorCode errorCode, String errorMessage) {
    return new WitchApiResponse<>(false, WitchError.of(errorCode, errorMessage));
  }

}
