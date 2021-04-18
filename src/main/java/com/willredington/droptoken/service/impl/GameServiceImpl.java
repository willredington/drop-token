package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.CreateGameRequest;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.exception.GameStatusException;
import com.willredington.droptoken.exception.InvalidPlacementException;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.exception.PlayerTurnException;
import com.willredington.droptoken.repository.GameEventRepository;
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
  private final GameEventRepository gameEventRepository;

  public GameServiceImpl(
      GameRepository gameRepository,
      List<GridValidator> gridValidators,
      GameEventServiceImpl gameEventService,
      GameEventRepository gameEventRepository) {
    this.gameRepository = gameRepository;
    this.gridValidators = gridValidators;
    this.gameEventService = gameEventService;
    this.gameEventRepository = gameEventRepository;
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

  private boolean isPlayersTurn(Game game, String playerId) {

    if (!game.getPlayers().contains(playerId)) {
      throw new NotFoundException(String.format("player %s not associated with game", playerId));
    }

    List<GameEvent> gameEvents = gameEventRepository.findAllByGameIdEquals(game.getId());

    // if we don't have any events, make sure the player is the first player
    if (gameEvents.isEmpty()) {
      return !game.getPlayers().isEmpty() && game.getPlayers().get(0).equals(playerId);
    }

    // make sure the last event wasn't by the incoming player
    GameEvent lastEvent = gameEvents.get(gameEvents.size() - 1);

    return !lastEvent.getPlayerId().equals(playerId);
  }

  public void checkBoardFull(String gameId) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    if (game.getStatus() != Status.COMPLETE) {

      boolean isFull = true;

      String[][] board = game.getBoard();

      for (int r = 0; r < game.getRows(); r++) {

        for (int c = 0; c < game.getColumns(); c++) {

          if (board[r][c].equals("")) {
            isFull = false;
            break;
          }
        }
      }

      if (isFull) {
        game.setStatus(Status.COMPLETE);
        gameRepository.save(game);
      }
    }
  }

  public Game create(CreateGameRequest dto) {
    return gameRepository.save(
        Game.builder()
            .columns(dto.getColumns())
            .rows(dto.getRows())
            .players(dto.getPlayerNames())
            .status(Status.IN_PROGRESS)
            .board(createEmptyBoard(dto.getRows(), dto.getColumns()))
            .build());
  }

  public GameEvent move(String gameId, String playerId, int column) {

    Game game = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);

    if (game.getStatus() == Status.COMPLETE) {
      throw new GameStatusException("game is already complete");
    }

    if (!isPlayersTurn(game, playerId)) {
      throw new PlayerTurnException("not players' turn");
    }

    if (column < 0 || column >= game.getColumns()) {
      throw new InvalidPlacementException("move is out of bounds");
    }

    String[][] board = game.getBoard();

    // find the first empty cell
    for (int rowNum = game.getRows() - 1; rowNum > -1; rowNum--) {

      String cellValue = board[rowNum][column];

      if (cellValue.equals("")) {

        board[rowNum][column] = playerId;

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
