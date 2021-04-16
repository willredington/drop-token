package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.exception.GameStatusException;
import com.willredington.droptoken.exception.InvalidPlacementException;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.type.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

  @Mock private GameRepository gameRepository;

  @Mock private GameEventRepository gameEventRepository;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private GameEventServiceImpl gameEventService;

  @Test
  void shouldNotMoveIfGameAlreadyComplete() {

    when(gameRepository.findById(anyString()))
        .thenReturn(Optional.of(Game.builder().status(Status.COMPLETE).build()));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    Assertions.assertThrows(GameStatusException.class, () -> service.move("game1", "player1", 0));
  }

  @Test
  void shouldNotMoveIfOutOfBounds() {

    when(gameRepository.findById(anyString()))
        .thenReturn(
            Optional.of(
                Game.builder()
                    .status(Status.IN_PROGRESS)
                    .rows(4)
                    .columns(4)
                    .players(Arrays.asList("player1", "player2"))
                    .build()));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    Assertions.assertThrows(
        InvalidPlacementException.class, () -> service.move("game1", "player1", 100));
  }

  @Test
  void shouldNotMoveBecauseTooFull() {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        Game.builder()
            .status(Status.IN_PROGRESS)
            .rows(4)
            .columns(4)
            .players(Arrays.asList("player1", "player2"))
            .board(board)
            .build();

    when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    Assertions.assertThrows(
        InvalidPlacementException.class, () -> service.move("game1", "player1", 0));
  }

  @Test
  void shouldMove() {

    String[][] initialBoard = {
      {"", "", "", ""},
      {"", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    String[][] expectedBoard = {
      {"", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        Game.builder()
            .status(Status.IN_PROGRESS)
            .rows(4)
            .columns(4)
            .players(Arrays.asList("player1", "player2"))
            .board(initialBoard)
            .build();

    when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    service.move("game1", "player1", 0);

    verify(gameRepository, times(1))
        .save(
            Game.builder()
                .status(Status.IN_PROGRESS)
                .rows(4)
                .columns(4)
                .players(Arrays.asList("player1", "player2"))
                .board(expectedBoard)
                .build());
  }

  @Test
  void shouldCheckBoardNotFull() {

    String[][] board = {
      {"player2", "player1", "player2", "player2"},
      {"player2", "player1", "player2", "player1"},
      {"player1", "player2", "player2", "player2"},
      {"player1", "player2", "player1", ""},
    };

    Game game =
        Game.builder()
            .status(Status.IN_PROGRESS)
            .rows(4)
            .columns(4)
            .players(Arrays.asList("player1", "player2"))
            .board(board)
            .build();

    when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    service.checkBoardFull("game1");

    verify(gameRepository, times(0)).save(game);
  }

  @Test
  void shouldCheckBoardFull() {

    String[][] board = {
      {"player2", "player1", "player2", "player2"},
      {"player2", "player1", "player2", "player1"},
      {"player1", "player2", "player2", "player2"},
      {"player1", "player2", "player1", "player1"},
    };

    Game game =
        Game.builder()
            .status(Status.IN_PROGRESS)
            .rows(4)
            .columns(4)
            .players(Arrays.asList("player1", "player2"))
            .board(board)
            .build();
    when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

    GameServiceImpl service =
        new GameServiceImpl(
            gameRepository, Collections.emptyList(), gameEventService, gameEventRepository);

    service.checkBoardFull("game1");

    Assertions.assertEquals(Status.COMPLETE, game.getStatus());
    verify(gameRepository, times(1)).save(game);
  }
}
