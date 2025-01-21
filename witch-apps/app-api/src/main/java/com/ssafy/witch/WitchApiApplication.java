package com.ssafy.witch;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Getter
@SpringBootApplication
public class WitchApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(WitchApiApplication.class, args);
  }

}
