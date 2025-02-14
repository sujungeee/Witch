package com.ssafy.witch.repository.snack;

import static com.querydsl.core.types.Projections.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.Snack.QSnackEntity;
import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SnackCustomRepositoryImpl implements SnackCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public SnackDetailEntityProjection getSnackDetail(String snackId) {
    QSnackEntity snack = QSnackEntity.snackEntity;

    return queryFactory
        .select(constructor(SnackDetailEntityProjection.class,
            snack.snackId,
            snack.userId,
            snack.longitude,
            snack.latitude,
            snack.snackImageUrl,
            snack.snackSoundUrl,
            snack.createdAt
        ))
        .from(snack)
        .where(snack.snackId.eq(snackId))
        .fetchOne();
  }
}
