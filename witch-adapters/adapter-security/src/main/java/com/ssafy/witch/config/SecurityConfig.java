package com.ssafy.witch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.filter.JsonLoginProcessingFilter;
import com.ssafy.witch.handler.ErrorResponseAuthenticationFailureHandler;
import com.ssafy.witch.handler.JwtAuthenticationSuccessHandler;
import com.ssafy.witch.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final ObjectMapper objectMapper;

  private final JwtService jwtService;

  private final AuthenticationConfiguration authenticationConfiguration;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(auth -> {
      auth
          .requestMatchers(HttpMethod.GET, "/users/email/is-unique").permitAll()
          .requestMatchers(HttpMethod.GET, "/users/nickname/is-unique").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code/confirm").permitAll()
          .requestMatchers(HttpMethod.POST, "/users").permitAll()
          .anyRequest().authenticated();
    });

    http.addFilterAt(jsonLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AbstractAuthenticationProcessingFilter jsonLoginProcessingFilter() throws Exception {
    JsonLoginProcessingFilter jsonLoginProcessingFilter =
        new JsonLoginProcessingFilter(objectMapper);

    jsonLoginProcessingFilter.setAuthenticationManager(authenticationManager());
    jsonLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    jsonLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

    return jsonLoginProcessingFilter;
  }


  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new JwtAuthenticationSuccessHandler(jwtService, objectMapper);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new ErrorResponseAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
