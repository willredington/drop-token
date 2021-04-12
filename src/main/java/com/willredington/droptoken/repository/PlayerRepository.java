package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

  Optional<Player> findByNameEquals(String playerName);
}
