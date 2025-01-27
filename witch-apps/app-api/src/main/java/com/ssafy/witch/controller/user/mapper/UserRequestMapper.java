package com.ssafy.witch.controller.user.mapper;

import com.ssafy.witch.controller.user.request.UserEmailVerificationCodeRequest;
import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

  CreateUserEmailVerificationCodeCommand toCommand(UserEmailVerificationCodeRequest request);

}
