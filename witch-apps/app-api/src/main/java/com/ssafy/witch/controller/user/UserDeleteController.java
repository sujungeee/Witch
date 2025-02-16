package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.UserDeleteRequest;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserDeleteController {

  private final DeleteUserUseCase deleteUserUseCase;

  private final UserRequestMapper userRequestMapper;

  @DeleteMapping("users/me")
  public WitchApiResponse<Void> deleteUser(@AuthenticationPrincipal String userId,
      @RequestBody UserDeleteRequest request) {

    deleteUserUseCase.delete(userRequestMapper.toCommand(userId, request));
    return WitchApiResponse.success();
  }
}
