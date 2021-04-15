package com.willredington.droptoken.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListGameResponse {

  private List<String> games;
}
