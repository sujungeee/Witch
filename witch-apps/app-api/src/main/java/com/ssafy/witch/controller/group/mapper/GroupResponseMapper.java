package com.ssafy.witch.controller.group.mapper;

import com.ssafy.witch.controller.group.response.GroupPreviewResponse;
import com.ssafy.witch.group.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupResponseMapper {

  GroupPreviewResponse toPreviewResponse(Group group);
}
