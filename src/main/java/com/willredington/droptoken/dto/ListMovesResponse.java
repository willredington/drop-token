package com.willredington.droptoken.dto;

import com.willredington.droptoken.entity.GameEvent;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListMovesResponse {

  private List<GameEvent> moves;
}
