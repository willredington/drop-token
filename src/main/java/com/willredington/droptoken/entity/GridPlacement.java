package com.willredington.droptoken.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grid_placement")
public class GridPlacement {

  @Id @GeneratedValue private Long id;

  @Column private String name;

  @Column(name = "grid_id")
  private Long gridId;

  @Column private Integer x;

  @Column private Integer y;
}
