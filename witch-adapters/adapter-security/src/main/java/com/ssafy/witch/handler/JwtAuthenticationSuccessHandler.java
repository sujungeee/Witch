package com.ssafy.witch.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.response.WitchApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements
    AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String email = userDetails.getUsername();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::toString)
        .toList();

    TokenResponse tokenResponse = jwtService.create(email, roles);

    sendResponse(response, tokenResponse);
  }

  private void sendResponse(HttpServletResponse response, TokenResponse tokenResponse)
      throws IOException {
    String body = objectMapper.writeValueAsString(WitchApiResponse.success(tokenResponse));

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(body);
    response.setStatus(HttpStatus.OK.value());
  }

}
