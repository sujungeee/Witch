package com.ssafy.witch.repository.snack;

import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;

public interface SnackCustomRepository {

  boolean isOwnerByUserIdAndSnackId(String userId, String snackId);
}
