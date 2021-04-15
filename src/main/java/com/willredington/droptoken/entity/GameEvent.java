package com.willredington.droptoken.entity;

import com.willredington.droptoken.type.Event;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@Document
public class GameEvent {

  @Id private String id;

  private Event type;

  private String playerId;

  private String gameId;

  private Map<String, Object> properties;
}
