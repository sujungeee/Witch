package com.ssafy.witch.repository.user;

import com.ssafy.witch.user.ValidateUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository implements ValidateUserPort {

  private final UserJpaRepository userJpaRepository;

  @Override
  public boolean isEmailDuplicated(String email) {
    return userJpaRepository.existsByEmail(email);
  }

}
