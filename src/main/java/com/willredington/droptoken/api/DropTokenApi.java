package com.willredington.droptoken.api;

import com.willredington.droptoken.dto.CreateGameRequest;
import com.willredington.droptoken.dto.CreateGameResponse;
import com.willredington.droptoken.dto.ListGameResponse;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.service.impl.GameServiceImpl;
import com.willredington.droptoken.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("drop_token")
public class DropTokenApi {

  private final GameServiceImpl gameService;
  private final GameRepository gameRepository;

  public DropTokenApi(GameServiceImpl gameService, GameRepository gameRepository) {
    this.gameService = gameService;
    this.gameRepository = gameRepository;
  }

  @GetMapping(value = "/")
  public ListGameResponse listGames() {

    List<String> gameIdsInProgress =
        gameRepository.findAllByStatusEquals(Status.IN_PROGRESS).stream()
            .map(Game::getId)
            .collect(Collectors.toList());

    return ListGameResponse.builder().games(gameIdsInProgress).build();
  }

  @PostMapping(value = "/")
  public CreateGameResponse createGame(@RequestBody CreateGameRequest request) {
    Game game = gameService.create(request);
    return CreateGameResponse.builder().gameId(game.getId()).build();
  }

  @GetMapping(value = "/{gameId}")
  public Game status(@PathVariable String gameId) {
    return gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
  }

  @GetMapping(value = "/{gameId}/{playerId}")
  public void move(@PathVariable String gameId, @PathVariable String playerId) {

    return;
  }
}
