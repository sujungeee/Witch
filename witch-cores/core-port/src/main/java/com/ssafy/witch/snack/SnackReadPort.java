package com.ssafy.witch.snack;

import com.ssafy.witch.snack.model.SnackDetailProjection;

public interface SnackReadPort {

  SnackDetailProjection getSnackDetail(String snackId);
}
