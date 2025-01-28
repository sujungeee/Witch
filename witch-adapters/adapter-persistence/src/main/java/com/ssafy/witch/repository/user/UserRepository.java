package com.ssafy.witch.repository.user;

import com.ssafy.witch.repository.user.mapper.UserEntityMapper;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.UserPort;
import com.ssafy.witch.user.ValidateUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository implements ValidateUserPort, UserPort {

  private final UserJpaRepository userJpaRepository;
  private final UserEntityMapper userEntityMapper;

  @Override
  public boolean isEmailDuplicated(String email) {
    return userJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean isNicknameDuplicated(String nickname) {
    return userJpaRepository.existsByNickname(nickname);
  }

  @Override
  public User save(User user) {
    return userEntityMapper.toDomain(userJpaRepository.save(userEntityMapper.toEntity(user)));
  }
}
