package com.ssafy.witch.controller.group;

import com.ssafy.witch.controller.group.mapper.GroupResponseMapper;
import com.ssafy.witch.controller.group.response.GroupPreviewResponse;
import com.ssafy.witch.group.Group;
import com.ssafy.witch.group.GroupReadUseCase;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupReadController {

  private final GroupReadUseCase groupReadUseCase;
  private final GroupResponseMapper groupResponseMapper;

  @GetMapping("/groups/{groupId}/preview")
  public WitchApiResponse<GroupPreviewResponse> getGroupPreview(
      @PathVariable String groupId) {
    Group groupPreview = groupReadUseCase.getGroupPreview(groupId);
    GroupPreviewResponse response = groupResponseMapper.toPreviewResponse(groupPreview);
    return WitchApiResponse.success(response);
  }
}
