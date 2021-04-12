package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Player;
import com.willredington.droptoken.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerServiceImpl {

  private final PlayerRepository playerRepository;

  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public Player getOrCreate(String playerName) {
    return playerRepository
        .findByNameEquals(playerName)
        .orElseGet(() -> playerRepository.save(Player.builder().name(playerName).build()));
  }
}
