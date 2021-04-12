package com.willredington.droptoken.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
public class GamePlayer {

  @Id @GeneratedValue private Long id;

  @Column(name = "game_id")
  private Long gameId;

  @Column(name = "player_id")
  private Long playerId;
}
