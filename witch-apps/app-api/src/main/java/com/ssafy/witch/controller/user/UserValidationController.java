package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.WitchApiResponse;
import com.ssafy.witch.user.ValidateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserValidationController {

  private final ValidateUserUseCase validateUserUseCase;

  @GetMapping("/users/email/is-unique")
  public WitchApiResponse<Void> checkEmailUnique(@RequestParam String email) {
    validateUserUseCase.checkUserEmailDuplication(email);
    return WitchApiResponse.success();
  }

}
