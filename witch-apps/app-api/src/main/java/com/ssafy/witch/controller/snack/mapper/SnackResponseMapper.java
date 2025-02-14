package com.ssafy.witch.controller.snack.mapper;

import com.ssafy.witch.controller.snack.response.SnackDetailResponse;
import com.ssafy.witch.snack.Snack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackResponseMapper {

  SnackDetailResponse toDetailResponse(Snack snack);
}
