package com.ssafy.witch.controller.group.mapper;

import com.ssafy.witch.controller.group.request.GroupCreateRequest;
import com.ssafy.witch.group.command.GroupCreateCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupRequestMapper {

  GroupCreateCommand toCommand(String userId, GroupCreateRequest request);
}
