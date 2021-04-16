package com.willredington.droptoken.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PlayerTurnException extends RuntimeException {

  public PlayerTurnException() {}

  public PlayerTurnException(String message) {
    super(message);
  }
}
