package com.ssafy.witch.repository.group;

import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import java.util.List;

public interface GroupCustomRepository {

  List<GroupWithLeaderProjection> findGroupListReadModelsByUserId(String userId);
}
