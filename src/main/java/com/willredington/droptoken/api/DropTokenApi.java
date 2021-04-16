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
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
  public Game status(@PathVariable String gameId) {
    return gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
  }

  @PostMapping(value = "drop_token/{gameId}/{playerId}")
  public PlayerMoveResponse move(
      @PathVariable String gameId,
      @PathVariable String playerId,
      @RequestBody PlayerMoveRequest request) {

    GameEvent moveEvent = gameService.move(gameId, playerId, request.getColumn());
    gameService.checkWinner(gameId);

    return PlayerMoveResponse.builder()
        .move(String.format("%s/moves/%s", gameId, moveEvent.getId()))
        .build();
  }

  @GetMapping(value = "drop_token/{gameId}/moves/{moveId}")
  public GameEvent move(@PathVariable String gameId, @PathVariable String moveId) {
    return gameEventRepository.findById(moveId).orElseThrow(NotFoundException::new);
  }

  @GetMapping(value = "drop_token/{gameId}/moves")
  public List<GameEvent> move(@PathVariable String gameId) {
    return gameEventRepository.findAllByGameIdEquals(gameId);
  }
}
