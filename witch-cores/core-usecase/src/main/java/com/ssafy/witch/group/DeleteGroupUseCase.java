package com.ssafy.witch.group;

import com.ssafy.witch.group.command.GroupDeleteCommand;

public interface DeleteGroupUseCase {
    void deleteGroup(GroupDeleteCommand command);
}
