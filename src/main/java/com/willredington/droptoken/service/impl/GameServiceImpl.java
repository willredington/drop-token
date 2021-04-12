package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.CreateGameDto;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.entity.GamePlayer;
import com.willredington.droptoken.entity.Grid;
import com.willredington.droptoken.entity.Player;
import com.willredington.droptoken.repository.GamePlayerRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.repository.GridRepository;
import com.willredington.droptoken.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameServiceImpl {

  private final PlayerServiceImpl playerService;
  private final GameRepository gameRepository;
  private final GridRepository gridRepository;
  private final GamePlayerRepository gamePlayerRepository;

  public GameServiceImpl(
      PlayerServiceImpl playerService,
      GameRepository gameRepository,
      GridRepository gridRepository,
      GamePlayerRepository gamePlayerRepository) {
    this.playerService = playerService;
    this.gameRepository = gameRepository;
    this.gridRepository = gridRepository;
    this.gamePlayerRepository = gamePlayerRepository;
  }

  private void assignPlayer(Long gameId, Long playerId) {
    gamePlayerRepository.save(GamePlayer.builder().gameId(gameId).playerId(playerId).build());
  }

  public Game createGame(CreateGameDto dto) {

    Game game = gameRepository.save(Game.builder().status(Status.IDLE).build());

    gridRepository.save(
        Grid.builder().gameId(game.getId()).rows(dto.getRows()).columns(dto.getColumns()).build());

    for (String playerName : dto.getPlayerNames()) {
      Player player = playerService.getOrCreate(playerName);
      assignPlayer(game.getId(), player.getId());
    }

    return game;
  }
}
