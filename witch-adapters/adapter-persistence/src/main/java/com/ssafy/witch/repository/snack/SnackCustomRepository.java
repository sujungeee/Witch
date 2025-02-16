package com.ssafy.witch.repository.snack;

import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;
import com.ssafy.witch.entity.Snack.SnackEntityProjection;
import java.util.List;

public interface SnackCustomRepository {

  List<SnackEntityProjection> getSnacks(String userId, String appointmentId);

}
