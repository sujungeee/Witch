package com.ssafy.witch.controller.group;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.file.response.PresignedUrlResponse;
import com.ssafy.witch.group.GroupImageUseCase;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupImageController {

  private final GroupImageUseCase groupImageUseCase;

  private final FileRequestMapper fileRequestMapper;
  private final FileResponseMapper fileResponseMapper;

  @PostMapping("/groups/presigned-url")
  public WitchApiResponse<PresignedUrlResponse> generatePresignedUrl(
      @AuthenticationPrincipal String userId,
      @RequestBody PresignedUrlRequest request) {
    GeneratePresignedUrlCommand command = fileRequestMapper.toCommand(userId, request);
    PresignedUrl presignedUrl = groupImageUseCase.generateGroupImagePresignedUrl(command);
    PresignedUrlResponse response = fileResponseMapper.toResponse(presignedUrl);

    return WitchApiResponse.success(response);
  }

}
