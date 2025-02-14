package com.ssafy.witch.snack;

import java.util.Optional;

public interface SnackPort {

  Snack save(Snack snack);

  boolean existsById(String snackId);

  Optional<Snack> findById(String snackId);

}
