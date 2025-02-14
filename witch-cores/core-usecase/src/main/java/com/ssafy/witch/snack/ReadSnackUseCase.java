package com.ssafy.witch.snack;

import com.ssafy.witch.snack.output.SnackDetailOutput;

public interface ReadSnackUseCase {

  SnackDetailOutput getSnackDetail(String userId, String snackId);
}
