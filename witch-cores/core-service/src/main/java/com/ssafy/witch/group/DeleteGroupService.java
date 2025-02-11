package com.ssafy.witch.group;

import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.GroupNotJoinedException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.group.command.GroupDeleteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteGroupService implements DeleteGroupUseCase {

    private final GroupPort groupPort;
    private final GroupMemberPort groupMemberPort;

    @Transactional
    @Override
    public void deleteGroup(GroupDeleteCommand command) {
        String userId = command.getUserId();
        String groupId = command.getGroupId();


        // 존재하지 않는 모임에 대한 요청입니다.
        Group group = groupPort.findById(groupId).orElseThrow(GroupNotFoundException::new);

        // 해당 모임에 대한 권한이 없습니다.
        GroupMember groupMember = groupMemberPort.findByUserIdAndGroupId(userId,
                        groupId)
                .orElseThrow(GroupNotJoinedException::new);
//        if (!groupMemberPort.existsByUserIdAndGroupId(userId, groupId)) {
//            throw new GroupNotJoinedException(); // 예외 던지기
//        }

        // 모임장 여부
        validateUserIsLeader(groupMember);

        groupMemberPort.deleteAllByGroupId(groupId);
        groupPort.delete(group);
    }

    private void validateUserIsLeader(GroupMember groupMember) {
        if (!groupMember.getIsLeader()) {
            throw new UnauthorizedGroupAccessException();
        }
    }
}
