package com.willredington.droptoken.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_move_event_detail")
public class GameMoveEventDetail {

  @Id @GeneratedValue private Long id;

  @Column(name = "game_event_id")
  private Long gameEventId;

  @Column private Integer row;

  @Column private Integer column;
}
