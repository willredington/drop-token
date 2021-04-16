package com.willredington.droptoken.api;

import com.willredington.droptoken.dto.*;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.service.impl.GameServiceImpl;
import com.willredington.droptoken.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DropTokenApi {

  private final GameServiceImpl gameService;
  private final GameRepository gameRepository;
  private final GameEventRepository gameEventRepository;

  public DropTokenApi(
      GameServiceImpl gameService,
      GameRepository gameRepository,
      GameEventRepository gameEventRepository) {
    this.gameService = gameService;
    this.gameRepository = gameRepository;
    this.gameEventRepository = gameEventRepository;
  }

  @GetMapping(value = "drop_token")
  public ListGameResponse listGames() {

    List<String> gameIdsInProgress =
        gameRepository.findAllByStatusEquals(Status.IN_PROGRESS).stream()
            .map(Game::getId)
            .collect(Collectors.toList());

    return ListGameResponse.builder().games(gameIdsInProgress).build();
  }

  @PostMapping(value = "drop_token")
  public CreateGameResponse createGame(@RequestBody CreateGameRequest request) {
    Game game = gameService.create(request);
    return CreateGameResponse.builder().gameId(game.getId()).build();
  }

  @GetMapping(value = "drop_token/{gameId}")
  public Game getGame(@PathVariable String gameId) {
    return gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
  }

  @PostMapping(value = "drop_token/{gameId}/{playerId}")
  public PlayerMoveResponse createMove(
      @PathVariable String gameId,
      @PathVariable String playerId,
      @RequestBody PlayerMoveRequest request) {

    GameEvent moveEvent = gameService.move(gameId, playerId, request.getColumn());
    gameService.checkWinner(gameId);
    gameService.checkBoardFull(gameId);

    return PlayerMoveResponse.builder()
        .move(String.format("%s/moves/%s", gameId, moveEvent.getId()))
        .build();
  }

  @GetMapping(value = "drop_token/{gameId}/moves/{moveId}")
  public GameEvent getMove(@PathVariable String gameId, @PathVariable String moveId) {

    if (!gameRepository.existsByIdEquals(gameId)) {
      throw new NotFoundException(String.format("game %s does not exist", gameId));
    }

    return gameEventRepository.findById(moveId).orElseThrow(NotFoundException::new);
  }

  @GetMapping(value = "drop_token/{gameId}/moves")
  public ListMovesResponse listMoves(
      @PathVariable String gameId,
      @RequestParam(required = false) Integer start,
      @RequestParam(required = false) Integer until) {

    List<GameEvent> allMoves = gameEventRepository.findAllByGameIdEquals(gameId);

    if (Objects.nonNull(start) && Objects.nonNull(until)) {
      return ListMovesResponse.builder().moves(allMoves.subList(start, until)).build();
    }

    return ListMovesResponse.builder().moves(allMoves).build();
  }

  @DeleteMapping(value = "drop_token/{gameId}/{playerId}")
  public ResponseEntity<?> removePlayer(
      @PathVariable String gameId, @PathVariable String playerId) {
    gameService.removePlayer(gameId, playerId);
    gameService.checkWinner(gameId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }
}
