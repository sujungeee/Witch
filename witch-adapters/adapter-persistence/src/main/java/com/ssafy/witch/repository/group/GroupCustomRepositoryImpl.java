package com.ssafy.witch.repository.group;

import static com.ssafy.witch.entity.group.QGroupEntity.groupEntity;
import static com.ssafy.witch.entity.group.QGroupMemberEntity.groupMemberEntity;
import static com.ssafy.witch.entity.user.QUserEntity.userEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.group.QGroupEntity;
import com.ssafy.witch.entity.group.QGroupMemberEntity;
import com.ssafy.witch.entity.user.QUserEntity;
import com.ssafy.witch.group.model.GroupDetailProjection;
import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import com.ssafy.witch.user.model.UserBasicProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupCustomRepositoryImpl implements GroupCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<GroupWithLeaderProjection> findGroupListReadModelsByUserId(String userId) {
    QGroupEntity group = groupEntity;
    QGroupMemberEntity groupMember = groupMemberEntity;
    QUserEntity user = userEntity;

    return queryFactory
        .select(Projections.constructor(
            GroupWithLeaderProjection.class,
            group.groupId,
            group.name,
            group.groupImageUrl,
            group.createdAt,
            Projections.constructor(
                UserBasicProjection.class,
                user.userId,
                user.nickname,
                user.profileImageUrl
            )
        ))
        .from(group)
        .join(groupMember).on(group.groupId.eq(groupMember.groupId))
        .join(user).on(groupMember.userId.eq(user.userId))
        .where(groupMember.userId.eq(userId).and(groupMember.isLeader.isTrue()))
        .fetch();
  }

  @Override
  public GroupDetailProjection readGroupDetail(String userId, String groupId) {

    return queryFactory
        .select(Projections.constructor(
            GroupDetailProjection.class,
            groupEntity.groupId,
            groupEntity.name,
            groupEntity.groupImageUrl,
            groupMemberEntity.isLeader,
            groupMemberEntity.cntLateArrival)
        )
        .from(groupEntity)
        .join(groupMemberEntity).on(groupEntity.groupId.eq(groupMemberEntity.groupId))
        .join(userEntity).on(groupMemberEntity.userId.eq(userEntity.userId))
        .where(groupEntity.groupId.eq(groupId).and(groupMemberEntity.userId.eq(userId)))
        .fetchOne();
  }
}
