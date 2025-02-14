package com.ssafy.witch.snack;

import com.ssafy.witch.snack.output.SnackDetailOutput;

public interface ReadSnackUseCase {

  Snack getSnackDetail(String userId, String snackId);
}
