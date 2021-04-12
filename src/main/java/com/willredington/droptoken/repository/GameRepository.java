package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

  boolean existsByIdEquals(Long gameId);
}
