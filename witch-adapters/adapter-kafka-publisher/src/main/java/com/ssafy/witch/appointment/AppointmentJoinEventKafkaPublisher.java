package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.AppointmentJoinEventPublishPort;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentJoinEventKafkaPublisher implements AppointmentJoinEventPublishPort {


  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void publish(AppointmentJoinEvent appointmentJoinEvent) {
    try {
      kafkaTemplate.send("appointment", objectMapper.writeValueAsString(appointmentJoinEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
