package com.ssafy.witch.user;

import com.ssafy.witch.common.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.user.command.UpdateProfileImageCommand;

public interface ProfileImageUseCase {

  PresignedUrl generateProfileImagePresignedUrl(GeneratePresignedUrlCommand command);

  void updateProfileImageUrl(UpdateProfileImageCommand command);
}
