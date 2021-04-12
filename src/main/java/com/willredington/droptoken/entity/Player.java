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
public class Player {

  @Id @GeneratedValue private Long id;

  @Column private String name;
}
