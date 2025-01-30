package com.ssafy.witch.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.jwt.JwtService;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.user.WitchUserDetails;
import com.ssafy.witch.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements
    AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    WitchUserDetails witchUserDetails = (WitchUserDetails) authentication.getPrincipal();

    String userId = witchUserDetails.getUserId();
    String email = witchUserDetails.getEmail();
    List<String> roles = witchUserDetails.getAuthorities().stream()
        .map(GrantedAuthority::toString)
        .toList();

    TokenResponse tokenResponse = jwtService.create(userId, email, roles);

    ResponseUtils.sendResponse(response, tokenResponse, objectMapper);
  }


}
