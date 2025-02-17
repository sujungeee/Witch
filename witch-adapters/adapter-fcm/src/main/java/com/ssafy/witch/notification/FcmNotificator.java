package com.ssafy.witch.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmNotificator {

  @Value("${witch.fcm.app-icon-url}")
  private String appIconUrl;

  public void sendNotification(WitchNotification witchNotification) {
    try {
      Message message = witchNotification.toMessage();
      FirebaseMessaging.getInstance().send(message);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
