package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

  List<GamePlayer> findAllByGameIdEquals(Long gameId);
}
