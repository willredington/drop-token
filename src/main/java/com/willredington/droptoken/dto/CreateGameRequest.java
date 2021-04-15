package com.willredington.droptoken.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGameRequest {

  @JsonProperty("players")
  private List<String> playerNames;

  @JsonProperty private Integer columns;

  @JsonProperty private Integer rows;
}
