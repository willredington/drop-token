package com.willredington.droptoken.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateGameResponse {

  private String gameId;
}
