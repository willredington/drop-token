package com.willredington.droptoken.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerMoveDto {

  private Long gameId;

  private Long playerId;

  private Integer column;

  private Integer row;
}
