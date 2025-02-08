package com.ssafy.witch.controller.group.mapper;

import com.ssafy.witch.controller.group.response.GroupListResponse;
import com.ssafy.witch.controller.group.response.GroupMemberListResponse;
import com.ssafy.witch.controller.group.response.GroupPreviewResponse;
import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.output.GroupWithLeaderListOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupResponseMapper {

  GroupPreviewResponse toPreviewResponse(Group group);

  GroupListResponse toGroupListResponse(GroupWithLeaderListOutput output);
}
