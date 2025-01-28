package com.ssafy.witch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(auth -> {
      auth
          .requestMatchers(HttpMethod.GET, "/users/email/is-unique").permitAll()
          .requestMatchers(HttpMethod.GET, "/users/nickname/is-unique").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code").permitAll()
          .requestMatchers(HttpMethod.POST, "/users/email-verification-code/confirm").permitAll()
          .requestMatchers(HttpMethod.POST, "/users").permitAll()
          .anyRequest().authenticated();
    });

    return http.build();
  }

}
