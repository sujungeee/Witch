package com.ssafy.witch.repository.user;

import com.ssafy.witch.entity.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<UserEntity> findByEmail(String email);

}
