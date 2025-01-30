package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.UserNicknameChangeRequest;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.ChangeUserInformationUseCase;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserInformationController {

  private final ChangeUserInformationUseCase changeUserInformationUseCase;
  private final UserRequestMapper userRequestMapper;

  @PatchMapping("/users/me/nickname")
  public WitchApiResponse<Void> changeNickname(
      @AuthenticationPrincipal String userId,
      @RequestBody UserNicknameChangeRequest request) {
    ChangeUserNicknameCommand command = userRequestMapper.toCommand(userId, request);
    changeUserInformationUseCase.changeUserNickname(command);
    return WitchApiResponse.success();
  }
}
