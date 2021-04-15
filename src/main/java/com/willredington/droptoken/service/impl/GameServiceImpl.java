package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.CreateGameRequest;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.service.GridValidator;
import com.willredington.droptoken.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GameServiceImpl {

  private final GameRepository gameRepository;
  private final List<GridValidator> gridValidators;

  public GameServiceImpl(GameRepository gameRepository, List<GridValidator> gridValidators) {
    this.gameRepository = gameRepository;
    this.gridValidators = gridValidators;
  }

  public Game create(CreateGameRequest dto) {
    return gameRepository.save(
        Game.builder()
            .columns(dto.getColumns())
            .rows(dto.getRows())
            .players(dto.getPlayerNames())
            .status(Status.IDLE)
            .board(new String[dto.getRows()][dto.getColumns()])
            .build());
  }

  public void checkWinner(String gameId) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    for (GridValidator gridValidator : gridValidators) {

      Optional<String> winnerOpt = gridValidator.findWinner(game);

      if (winnerOpt.isPresent()) {
        String winner = winnerOpt.get();
        log.info("found winner: {}", winner);
        // TODO: update game
        // stop iteration
      }
    }
  }
}
