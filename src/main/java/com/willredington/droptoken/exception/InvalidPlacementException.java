package com.willredington.droptoken.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidPlacementException extends RuntimeException {

  public InvalidPlacementException() {}

  public InvalidPlacementException(String message) {
    super(message);
  }
}
