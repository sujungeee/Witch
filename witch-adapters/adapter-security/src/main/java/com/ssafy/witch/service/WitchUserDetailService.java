package com.ssafy.witch.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WitchUserDetailService implements UserDetailsService {

  private final UserPort userPort;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userPort.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

    return builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .build();
  }
}
