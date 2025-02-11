package com.ssafy.witch.controller.group;

import com.ssafy.witch.group.DeleteGroupUseCase;
import com.ssafy.witch.group.command.GroupDeleteCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupDeleteController {

    private final DeleteGroupUseCase deleteGroupUseCase;

    @DeleteMapping("/groups/{groupId}")
    public WitchApiResponse deleteGroup(
            @AuthenticationPrincipal String userId,
            @PathVariable("groupId") String groupId) {
        GroupDeleteCommand command = new GroupDeleteCommand(userId, groupId);
        deleteGroupUseCase.deleteGroup(command);
        return WitchApiResponse.success();

    }

}
