package com.willredington.droptoken.entity;

import com.willredington.droptoken.type.Status;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class Game {

  @Id @GeneratedValue private Long id;

  @Column private String name;

  @Column
  @Enumerated(EnumType.ORDINAL)
  private Status status;
}
