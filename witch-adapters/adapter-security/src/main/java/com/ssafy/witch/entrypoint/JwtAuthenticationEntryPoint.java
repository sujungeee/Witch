package com.ssafy.witch.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.exception.ErrorCode;
import com.ssafy.witch.exception.auth.JwtAuthException;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.response.WitchError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import util.JwtConst;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    JwtAuthException jwtAuthException =
        (JwtAuthException) request.getAttribute(JwtConst.JWT_AUTH_EXCEPTION_ATTRIBUTE_KEY);

    if (Objects.nonNull(jwtAuthException)) {
      sendUnauthorizedResponse(response, jwtAuthException.getErrorCode());
      return;
    }

    sendUnauthorizedResponse(response, ErrorCode.UNAUTHORIZED);
  }

  private void sendUnauthorizedResponse(HttpServletResponse response, ErrorCode errorCode)
      throws IOException {
    WitchApiResponse<WitchError> errorResponse = WitchApiResponse.failure(errorCode);

    String body = objectMapper.writeValueAsString(errorResponse);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
  }

}
