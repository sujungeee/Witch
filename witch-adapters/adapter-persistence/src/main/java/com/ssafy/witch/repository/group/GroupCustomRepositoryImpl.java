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
    QGroupMemberEntity leaderMember = new QGroupMemberEntity("leaderMember");
    QUserEntity leaderUser = new QUserEntity("leaderUser");

    return queryFactory
        .select(Projections.constructor(
            GroupWithLeaderProjection.class,
            group.groupId,
            group.name,
            group.groupImageUrl,
            group.createdAt,
            Projections.constructor(
                UserBasicProjection.class,
                leaderUser.userId,
                leaderUser.nickname,
                leaderUser.profileImageUrl
            )
        ))
        .from(group)
        .join(groupMember).on(group.groupId.eq(groupMember.groupId))
        .join(leaderMember)
        .on(group.groupId.eq(leaderMember.groupId).and(leaderMember.isLeader.isTrue()))
        .join(leaderUser).on(leaderMember.userId.eq(leaderUser.userId))
        .where(groupMember.userId.eq(userId))
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
