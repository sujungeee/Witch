package com.ssafy.witch.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GroupEventKafkaPublisher implements GroupEventPublishPort {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void publish(CreateGroupJoinRequestEvent event) {
    try {
      kafkaTemplate.send(GroupEventTopic.JOIN_REQUEST_GROUP,
          objectMapper.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }

  }
}
