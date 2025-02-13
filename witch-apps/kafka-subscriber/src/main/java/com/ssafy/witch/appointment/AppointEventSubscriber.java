package com.ssafy.witch.appointment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.apoointment.event.AppointmentJoinEvent;
import com.ssafy.witch.appointment.command.NotifyAppointmentJoinCommand;
import com.ssafy.witch.event.AppointmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointEventSubscriber {

  private final ObjectMapper objectMapper;
  private final NotifyAppointmentUseCase notifyAppointmentUseCase;

  @KafkaListener(topics = AppointmentEvent.JOIN_APPOINTMENT)
  public void handleAppointmentJoinEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      AppointmentJoinEvent appointmentJoinEvent = objectMapper.readValue(data.value(),
          AppointmentJoinEvent.class);

      notifyAppointmentUseCase.notifyJoin(new NotifyAppointmentJoinCommand(appointmentJoinEvent));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
