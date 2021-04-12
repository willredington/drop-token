package com.willredington.droptoken.entity;

import com.willredington.droptoken.type.Event;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class GameEvent {

  @Id @GeneratedValue private Long id;

  @Column(name = "game_id")
  private Long gameId;

  @Column(name = "player_id")
  private Long playerId;

  @Column
  @Enumerated(EnumType.ORDINAL)
  private Event event;
}
