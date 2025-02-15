package com.ssafy.witch.repository.snack;

import static com.querydsl.core.types.Projections.*;
import static com.ssafy.witch.entity.Snack.QSnackEntity.*;

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
  public boolean isOwnerByUserIdAndSnackId(String userId, String snackId) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(snackEntity)
        .where(
            snackEntity.userId.eq(userId),
            snackEntity.snackId.eq(snackId)
        )
        .fetchFirst();
    return fetchOne != null;
  }

}
