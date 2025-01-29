package com.ssafy.witch.user;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WitchUserDetails {

  private String email;
  private List<String> roles;

}
