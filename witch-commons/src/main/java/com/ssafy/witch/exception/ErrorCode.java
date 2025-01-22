package com.ssafy.witch.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 시스템 에러
  INTERNAL_SERVER_ERROR("WCH0000", "서버에 문제가 발생했습니다."),

  // 요청 검증 실패
  NOT_FOUND("WCH1000", "존재하지 않는 엔드포인트입니다."),
  METHOD_NOT_ALLOWED("WCH1001", "이 엔드포인트에서 헤당 메서드는 허용되지 않습니다."),
  REQUIRED_FIELD_MISSING("WCH1002", "필수 데이터가 누락되었습니다."),
  INVALID_FORMAT("WCH1003", "데이터 형식이 올바르지 않습니다."),
  UNSUPPORTED_FILE_FORMAT("WCH1004", "지원하지 않는 파일 형식입니다. JPG, PNG 형식만 지원됩니다."),
  FILE_SIZE_TOO_LARGE("WCH1005", "파일 크기가 너무 큽니다. 최대 5MB 까지 업로드 할 수 있습니다."),
  INVALID_REQUEST_BODY("WCH1006", "요청 바디의 타입 또는 형식이 올바르지 않습니다."),
  INVALID_PARAMETER("WCH1007", "요청 파라미터 타입이 올바르지 않습니다."),
  MISSING_REQUIRED_PARAMETER("WCH1008", "필수 파라미터가 존재하지 않습니다."),

  // 인증/인가
  INVALID_ACCESS_TOKEN("WCH2000", "access token이 유효하지 않습니다."),
  INVALID_REFRESH_TOKEN("WCH2001", "refresh token이 유효하지 않습니다."),
  REFRESH_TOKEN_NOT_UPDATABLE("WCH2002", "refresh token이 갱신 가능한 상태가 아닙니다."),
  INCORRECT_PASSWORD("WCH2003", "비밀번호가 올바르지 않습니다."),

  // 사용자
  EMAIL_ALREADY_IN_USE("WCH3000", "이미 사용 중인 이메일입니다."),
  NICKNAME_ALREADY_IN_USE("WCH3001", "이미 사용 중인 닉네임입니다."),
  EMAIL_VERIFICATION_CODE_MISMATCH("WCH3002", "이메일 인증 코드가 일치하지 않습니다."),

  // 모임
  NON_EXISTENT_MEETING("WCH4000", "존재하지 않는 모임에 대한 요청입니다."),
  MEETING_NAME_ALREADY_IN_USE("WCH4001", "이미 사용 중인 모임 이름입니다."),
  UNAUTHORIZED_MEETING_ACCESS("WCH4002", "해당 모임에 대한 권한이 없습니다."),
  EXCEED_MAX_MEETING_CAPACITY("WCH4003", "모임 최대 인원을 초과하였습니다."),
  ALREADY_JOINED_MEETING("WCH4004", "대상이 이미 가입한 모임입니다."),
  NOT_JOINED_MEETING("WCH4005", "대상이 이미 가입하지 않은 모임입니다."),

  // 약속
  INVALID_APPOINTMENT_TIME("WCH5000", "약속 시간은 10분 단위여야 합니다."),
  APPOINTMENT_TIME_IN_PAST("WCH5001", "약속 시간은 현재 시간 이전일 수 없습니다."),
  CONFLICTING_APPOINTMENT_TIME("WCH5002", "기존 약속과 시간이 겹칩니다."),
  UNAUTHORIZED_APPOINTMENT_ACCESS("WCH5003", "해당 약속에 대한 권한이 없습니다."),
  NON_EXISTENT_APPOINTMENT("WCH5006", "존재하지 않는 약속에 대한 요청입니다."),
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
