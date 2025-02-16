package com.ssafy.witch.group;

public interface NotifyGroupPort {

  void notifyJoinRequestCreated(JoinRequestCreateNotification notification);

  void notifyJoinRequestApproved(JoinRequestApprovedNotification notification);
}
