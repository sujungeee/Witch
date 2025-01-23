package com.ssafy.witch.repository.user;

import com.ssafy.witch.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {

  boolean existsByEmail(String email);

}
