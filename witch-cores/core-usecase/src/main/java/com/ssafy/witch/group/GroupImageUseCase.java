package com.ssafy.witch.group;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;

public interface GroupImageUseCase {

  PresignedUrl generateGroupImagePresignedUrl(GeneratePresignedUrlCommand command);
}
