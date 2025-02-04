package com.ssafy.witch.controller.group;

import com.ssafy.witch.group.CreateGroupJoinRequestUseCase;
import com.ssafy.witch.group.HandleGroupJoinRequestUseCase;
import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupJoinController {

  private final CreateGroupJoinRequestUseCase createGroupJoinRequestUseCase;
  private final HandleGroupJoinRequestUseCase handleGroupJoinRequestUseCase;

  @PostMapping("/groups/{groupId}/join-requests")
  public WitchApiResponse<Void> createJoinRequests(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {

    GroupJoinRequestCreateCommand command =
        new GroupJoinRequestCreateCommand(userId, groupId);

    createGroupJoinRequestUseCase.creatGroupJoinRequest(command);

    return WitchApiResponse.success();
  }

  @PostMapping("/groups/join-requests/{joinRequestId}/approve")
  public WitchApiResponse<Void> approveJoinRequest(
      @AuthenticationPrincipal String userId,
      @PathVariable("joinRequestId") String joinRequestId) {

    ApproveGroupJoinRequestCommand command = new ApproveGroupJoinRequestCommand(userId,
        joinRequestId);

    handleGroupJoinRequestUseCase.approveGroupJoinRequest(command);

    return WitchApiResponse.success();
  }

}
