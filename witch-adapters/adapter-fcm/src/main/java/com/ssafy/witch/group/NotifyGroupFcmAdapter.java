package com.ssafy.witch.group;

import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyGroupFcmAdapter implements NotifyGroupPort {

  private final FcmNotificator fcmNotificator;

  @Override
  public void notifyJoinRequestCreated(JoinRequestCreateNotification notification) {
    String title = String.format("[Witch] %s 모임으로 참여 요청이 도착했어요.", notification.getGroupName());
    String body = String.format("%s 님이 참여를 요청했어요!", notification.getRequestUserNickname());

    Map<String, String> data =
        createGroupData(GroupEventTopic.JOIN_REQUEST_GROUP, notification.getGroupId());

    String fcmToken = notification.getTargetUserFcmToken();
    WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
    fcmNotificator.sendNotification(witchNotification);
  }

  private Map<String, String> createGroupData(String groupType, String groupId) {
    return Map.of("type", groupType, "parameter", groupId);
  }

}
