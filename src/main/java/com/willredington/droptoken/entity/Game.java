package com.willredington.droptoken.entity;

import com.willredington.droptoken.type.Status;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
public class Game {

  @Id private String id;

  private Status status;

  private String winner;

  private String[][] board;

  private List<String> players;

  private Integer rows;

  private Integer columns;
}
