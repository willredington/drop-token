package com.willredington.droptoken.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayerMoveRequest {

  @JsonProperty private int column;
}
