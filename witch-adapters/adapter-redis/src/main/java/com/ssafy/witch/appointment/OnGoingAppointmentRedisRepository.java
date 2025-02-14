package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class OnGoingAppointmentRedisRepository implements OnGoingAppointmentCachePort {

  private static final String KEY_PREFIX = "onGoingAppointment:";

  private final ObjectMapper objectMapper;
  private final StringRedisTemplate redisTemplate;

  @Override
  public boolean has(AppointmentDetailProjection appointment) {
    String appointmentId = appointment.getAppointmentId();
    return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + appointmentId));
  }

  @Override
  public void save(AppointmentDetailProjection appointmentDetail, Duration duration) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    try {
      String value = objectMapper.writeValueAsString(appointmentDetail);
      ops.set(KEY_PREFIX + appointmentDetail.getAppointmentId(), value, duration);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
  }
}
