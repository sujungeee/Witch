package com.ssafy.witch.group;

import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.group.GroupNotFoundException;
import com.ssafy.witch.exception.group.UnauthorizedGroupAccessException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.group.command.UpdateGroupImageCommand;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class GroupImageServiceTest {

  @InjectMocks
  private GroupImageService sut;

  @Mock
  private FileOwnerCachePort fileOwnerCachePort;

  @Mock
  private GroupPort groupPort;

  @Mock
  private GroupMemberPort groupMemberPort;

  @Mock
  private PresignedUrlPort presignedUrlPort;

  //1. 파일 소유권 없으면 예외
  //2. 존재하는 모임 아니면 예외
  //3. 해당 모임에 권한 없으면 예외
  //4. 다 정상이면 저장 되는지 확인

  @Test
  void 파일에_대한_소유권이_없으면_사진_변경에_실패한다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String objectKey = "/object/key/example";

    BDDMockito.given(fileOwnerCachePort.getOwnerId(objectKey))
        .willThrow(InvalidFileOwnerException.class);
    //when&then
    Assertions.assertThatThrownBy(() -> {
      UpdateGroupImageCommand command = new UpdateGroupImageCommand(userId, groupId, objectKey);
      sut.updateGroupImageUrl(command);
    }).isInstanceOf(InvalidFileOwnerException.class);
  }

  @Test
  void 존재하지_않는_모임의_사진을_변경할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String objectKey = "/object/key/example";

    BDDMockito.given(fileOwnerCachePort.getOwnerId(objectKey)).willReturn(userId);

    BDDMockito.given(groupPort.findById(groupId)).willReturn(Optional.empty());
    //when&then
    Assertions.assertThatThrownBy(() -> {
      UpdateGroupImageCommand command = new UpdateGroupImageCommand(userId, groupId, objectKey);
      sut.updateGroupImageUrl(command);
    }).isInstanceOf(GroupNotFoundException.class);
  }

  @Test
  void 권한이_없는_모임의_사진을_변경할_수_없다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String objectKey = "/object/key/example";

    Group group = new Group(groupId, "any", "any");

    BDDMockito.given(fileOwnerCachePort.getOwnerId(objectKey)).willReturn(userId);

    BDDMockito.given(groupPort.findById(groupId)).willReturn(Optional.of(group));

    BDDMockito.given(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId))
        .willThrow(UnauthorizedGroupAccessException.class);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      UpdateGroupImageCommand command = new UpdateGroupImageCommand(userId, groupId, objectKey);
      sut.updateGroupImageUrl(command);
    }).isInstanceOf(UnauthorizedGroupAccessException.class);
  }

  @Test
  void 모임_이름_변경에_성공한다() {
    //given
    String userId = "test-user-id";
    String groupId = "test-group-id";
    String objectKey = "/object/key/example";
    String groupImageUrl = "group-new";

    Group group = new Group(groupId, "any", "group-original");

    BDDMockito.given(fileOwnerCachePort.getOwnerId(objectKey)).willReturn(userId);

    BDDMockito.given(groupPort.findById(groupId)).willReturn(Optional.of(group));

    BDDMockito.given(groupMemberPort.isLeaderByUserIdAndGroupId(userId, groupId)).willReturn(true);

    BDDMockito.given(presignedUrlPort.getAccessUrl(objectKey)).willReturn(groupImageUrl);

    //when&then
    Assertions.assertThatCode(() -> {
      UpdateGroupImageCommand command = new UpdateGroupImageCommand(userId, groupId, objectKey);
      sut.updateGroupImageUrl(command);
    }).doesNotThrowAnyException();

    Assertions.assertThat(group.getGroupImageUrl()).isEqualTo(groupImageUrl);
    BDDMockito.then(groupPort).should().save(group);
  }
}