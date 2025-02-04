package com.ssafy.witch.repository.group;

import static com.ssafy.witch.entity.group.QGroupMemberEntity.groupMemberEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupMemberCustomRepositoryImpl implements GroupMemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public boolean isLeaderByUserIdAndGroupId(String userId, String groupId) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(groupMemberEntity)
        .where(
            groupMemberEntity.userId.eq(userId),
            groupMemberEntity.groupId.eq(groupId),
            groupMemberEntity.isLeader.isTrue()
        )
        .fetchFirst();

    return fetchOne != null;
  }
}
