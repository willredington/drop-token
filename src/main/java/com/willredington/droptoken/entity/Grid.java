package com.willredington.droptoken.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grid")
public class Grid {

  @Id @GeneratedValue private Long id;

  @Column(name = "game_id")
  private Long gameId;

  @Column private Integer rows;

  @Column private Integer columns;
}
