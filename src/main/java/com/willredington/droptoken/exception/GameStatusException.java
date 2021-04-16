package com.willredington.droptoken.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class GameStatusException extends RuntimeException {

  public GameStatusException() {}

  public GameStatusException(String message) {
    super(message);
  }
}
