package com.ssafy.witch.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.group.command.NotifyGroupJoinRequestCommand;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GroupEventSubscriber {

  private final ObjectMapper objectMapper;
  private final NotifyGroupUseCase notifyGroupUseCase;

  @KafkaListener(topics = GroupEventTopic.GROUP_JOIN_REQUEST)
  public void handleGroupJoinRequestEvent(ConsumerRecord<String, String> data,
      Acknowledgment acknowledgment) {
    try {
      CreateGroupJoinRequestEvent event = objectMapper.readValue(data.value(),
          CreateGroupJoinRequestEvent.class);

      notifyGroupUseCase.notifyJoinRequest(new NotifyGroupJoinRequestCommand(event));
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }
}
