package com.ssafy.witch.group.mapper;

import com.ssafy.witch.group.model.GroupWithLeaderProjection;
import com.ssafy.witch.group.output.GroupWithLeaderListOutput;
import com.ssafy.witch.group.output.GroupWithLeaderOutput;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupOutputMapper {

  default GroupWithLeaderListOutput toOutput(List<GroupWithLeaderProjection> projections) {
    return new GroupWithLeaderListOutput(groupProjectionToOutputList(projections));
  }

  List<GroupWithLeaderOutput> groupProjectionToOutputList(
      List<GroupWithLeaderProjection> projections);
}
