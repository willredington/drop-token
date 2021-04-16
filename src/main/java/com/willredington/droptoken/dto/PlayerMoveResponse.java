package com.willredington.droptoken.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerMoveResponse {

  private String move;
}
