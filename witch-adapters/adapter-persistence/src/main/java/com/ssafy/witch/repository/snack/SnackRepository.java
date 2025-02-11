package com.ssafy.witch.repository.snack;

import com.ssafy.witch.mapper.snack.SnackMapper;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.snack.SnackPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SnackRepository implements SnackPort {

  private final SnackJpaRepository snackJpaRepository;

  private final SnackMapper snackMapper;

  @Override
  public Snack save(Snack snack) {
    return snackMapper.toDomain(snackJpaRepository.save(snackMapper.toEntity(snack)));
  }

}
