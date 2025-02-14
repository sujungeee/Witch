package com.ssafy.witch.mapper.snack;

import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;
import com.ssafy.witch.snack.model.SnackDetailProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackProjectionMapper {

  SnackDetailProjection toProjection(
      SnackDetailEntityProjection snackDetailEntityProjection
  );

}
