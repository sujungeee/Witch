package com.ssafy.witch.repository.appointment;

import static com.ssafy.witch.entity.appointment.QAppointmentEntity.appointmentEntity;
import static com.ssafy.witch.entity.appointment.QAppointmentMemberEntity.appointmentMemberEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
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
}
