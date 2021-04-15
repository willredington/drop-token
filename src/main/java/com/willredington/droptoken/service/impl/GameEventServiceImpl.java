package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.type.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class GameEventServiceImpl {

  private final GameEventRepository gameEventRepository;

  public GameEventServiceImpl(GameEventRepository gameEventRepository) {
    this.gameEventRepository = gameEventRepository;
  }

  public GameEvent createMoveEvent(String gameId, String playerId, Integer column) {
    return gameEventRepository.save(
        GameEvent.builder()
            .type(Event.MOVE)
            .playerId(playerId)
            .gameId(gameId)
            .properties(Collections.singletonMap("column", column))
            .build());
  }
}
