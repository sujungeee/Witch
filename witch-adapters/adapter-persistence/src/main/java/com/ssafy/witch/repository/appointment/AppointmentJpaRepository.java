package com.ssafy.witch.repository.appointment;

import com.ssafy.witch.entity.appointment.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends
    JpaRepository<AppointmentEntity, String>, AppointmentCustomRepository {

}
