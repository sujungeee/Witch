package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.entity.appointment.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AppointmentJpaRepository extends
    JpaRepository<AppointmentEntity, String>, AppointmentCustomRepository {

    @Transactional
    void deleteAllByGroupId(String groupId);
}
