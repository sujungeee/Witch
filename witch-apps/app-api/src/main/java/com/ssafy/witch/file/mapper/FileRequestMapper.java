package com.ssafy.witch.file.mapper;

import com.ssafy.witch.common.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileRequestMapper {

  GeneratePresignedUrlCommand toCommand(String userId, PresignedUrlRequest request);

}
