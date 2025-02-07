package com.ssafy.witch.group;

import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import java.util.List;

public interface GroupReadPort {

  List<GroupWithLeaderProjection> readGroupsWithLeaderByUserId(String userId);
}
