package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.CreateGameRequest;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.exception.GameStatusException;
import com.willredington.droptoken.exception.InvalidPlacementException;
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
  private final GameEventServiceImpl gameEventService;

  public GameServiceImpl(
      GameRepository gameRepository,
      List<GridValidator> gridValidators,
      GameEventServiceImpl gameEventService) {
    this.gameRepository = gameRepository;
    this.gridValidators = gridValidators;
    this.gameEventService = gameEventService;
  }

  private String[][] createEmptyBoard(int rows, int cols) {
    String[][] board = new String[rows][cols];

    for (int r = 0; r < rows; r++) {

      for (int c = 0; c < cols; c++) {
        board[r][c] = "";
      }
    }

    return board;
  }

  private boolean isBoardFull(Game game) {

    String[][] board = game.getBoard();

    for (int r = 0; r < game.getRows(); r++) {

      for (int c = 0; c < game.getColumns(); c++) {
        if (board[r][c].equals("")) {
          return false;
        }
      }
    }

    return true;
  }

  public Game create(CreateGameRequest dto) {
    return gameRepository.save(
        Game.builder()
            .columns(dto.getColumns())
            .rows(dto.getRows())
            .players(dto.getPlayerNames())
            .status(Status.IDLE)
            .board(createEmptyBoard(dto.getRows(), dto.getColumns()))
            .build());
  }

  public GameEvent move(String gameId, String playerId, int column) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    if (game.getStatus() == Status.COMPLETE) {
      throw new GameStatusException("game is already complete");
    }

    if (column < 0 || column >= game.getColumns()) {
      throw new InvalidPlacementException("move is out of bounds");
    }

    String[][] board = game.getBoard();

    for (int rowNum = game.getRows() - 1; rowNum > -1; rowNum--) {

      String cellValue = board[rowNum][column];

      if (cellValue.equals("")) {

        board[rowNum][column] = playerId;

        if (game.getStatus() != Status.IN_PROGRESS) {
          game.setStatus(Status.IN_PROGRESS);
        }

        if (isBoardFull(game)) {
          game.setStatus(Status.COMPLETE);
        }

        game.setBoard(board);
        gameRepository.save(game);

        return gameEventService.createMoveEvent(gameId, playerId, column);
      }
    }

    throw new InvalidPlacementException("column is at full capacity");
  }

  public void checkWinner(String gameId) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    if (game.getStatus() != Status.COMPLETE) {

      for (GridValidator gridValidator : gridValidators) {

        Optional<String> winnerOpt = gridValidator.findWinner(game);

        if (winnerOpt.isPresent()) {
          String winner = winnerOpt.get();
          game.setWinner(winner);
          game.setStatus(Status.COMPLETE);
          gameRepository.save(game);
          return;
        }
      }
    }
  }

  public void removePlayer(String gameId, String playerId) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    if (game.getStatus() == Status.COMPLETE) {
      throw new GameStatusException("game is already complete");
    }

    if (!game.getPlayers().contains(playerId)) {
      throw new NotFoundException("player not apart of game");
    }

    List<String> players = game.getPlayers();
    players.remove(playerId);

    game.setPlayers(players);

    if (players.size() == 1) {
      game.setStatus(Status.COMPLETE);
      game.setWinner(players.get(0));
    }

    gameRepository.save(game);
    gameEventService.createQuitEvent(gameId, playerId);
  }
}
