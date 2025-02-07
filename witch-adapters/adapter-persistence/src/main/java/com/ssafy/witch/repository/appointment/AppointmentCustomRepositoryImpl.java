package com.ssafy.witch.repository.appointment;

import static com.ssafy.witch.entity.appointment.QAppointmentEntity.appointmentEntity;
import static com.ssafy.witch.entity.appointment.QAppointmentMemberEntity.appointmentMemberEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.appointment.AppointmentEntityProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentCustomRepositoryImpl implements AppointmentCustomRepository {

  private final JPAQueryFactory queryFactory;

  public boolean existsConflictAppointment(String userId, LocalDateTime appointmentTime) {
    LocalDateTime oneHourLater = appointmentTime.plusMinutes(50);

    Integer fetchOne = queryFactory
        .selectOne()
        .from(appointmentMemberEntity)
        .join(appointmentEntity)
        .on(appointmentMemberEntity.appointmentId.eq(appointmentEntity.appointmentId))
        .where(
            appointmentMemberEntity.userId.eq(userId),
            appointmentEntity.appointmentTime.between(appointmentTime, oneHourLater)
        )
        .fetchFirst();

    return fetchOne != null; // 값이 있으면 true, 없으면 false
  }

  @Override
  public List<AppointmentEntityProjection> getAppointments(String userId, String groupId) {

    return queryFactory
        .select(Projections.constructor(AppointmentEntityProjection.class,
            appointmentEntity.appointmentId,
            appointmentEntity.name,
            appointmentEntity.appointmentTime,
            appointmentEntity.status,
            appointmentMemberEntity.userId.when(userId).then(true).otherwise(false)
        ))
        .from(appointmentEntity)
        .leftJoin(appointmentMemberEntity)
        .on(appointmentEntity.appointmentId.eq(appointmentMemberEntity.appointmentId)
            .and(appointmentMemberEntity.userId.eq(userId)))
        .where(appointmentEntity.groupId.eq(groupId))
        .fetch();
  }

}
