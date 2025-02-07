package com.ssafy.witch.repository.appointment;

import static com.ssafy.witch.entity.appointment.QAppointmentEntity.appointmentEntity;
import static com.ssafy.witch.entity.appointment.QAppointmentMemberEntity.appointmentMemberEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.witch.entity.appointment.AppointmentEntityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppointmentCustomRepositoryImpl implements AppointmentCustomRepository {

  private final JPAQueryFactory queryFactory;

  public boolean existsOngoingAppointmentByUserId(String userId) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(appointmentEntity, appointmentMemberEntity)
        .join(appointmentEntity)
        .on(appointmentMemberEntity.appointmentId.eq(appointmentEntity.appointmentId))
        .where(
            appointmentMemberEntity.userId.eq(userId),
            appointmentEntity.status.eq(AppointmentEntityStatus.ONGOING)
        )
        .fetchFirst();

    return fetchOne != null;
  }
}
