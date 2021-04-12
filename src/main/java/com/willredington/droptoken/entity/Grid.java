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

  @Column private String name;

  @Column(name = "game_id")
  private Long gameId;

  @Column(name = "x_dimension")
  private Integer xDimension;

  @Column(name = "y_dimension")
  private Integer yDimension;
}
