package com.ssafy.witch.advice;

import com.ssafy.witch.controller.WitchApiResponse;
import com.ssafy.witch.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected WitchApiResponse<?> handleException(Exception e) {
    log.error("error={}", e.getMessage(), e);
    return WitchApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR);
  }

}
