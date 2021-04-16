package com.willredington.droptoken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.willredington.droptoken.dto.CreateGameRequest;
import com.willredington.droptoken.dto.PlayerMoveRequest;
import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.type.Event;
import com.willredington.droptoken.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class AppIntegrationTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired private MockMvc mockMvc;
  @Autowired private GameRepository gameRepository;
  @Autowired private GameEventRepository gameEventRepository;

  @BeforeEach
  public void beforeEach() {
    gameRepository.deleteAll();
    gameEventRepository.deleteAll();
  }

  @Test
  void shouldCreateGame() throws Exception {

    CreateGameRequest request = new CreateGameRequest();
    request.setPlayerNames(Arrays.asList("player1", "player2"));
    request.setColumns(4);
    request.setRows(4);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(post("/drop_token").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.gameId").isString());
  }

  @Test
  void shouldNotCreateGameForBadRequest() throws Exception {
    mockMvc
        .perform(post("/drop_token").contentType(MediaType.APPLICATION_JSON).content(""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetInProgressGames() throws Exception {

    Game game = gameRepository.save(Game.builder().status(Status.IN_PROGRESS).build());

    mockMvc
        .perform(get("/drop_token").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.games[0]").value(game.getId()));
  }

  @Test
  void shouldGetGame() throws Exception {

    Game game = gameRepository.save(Game.builder().build());

    mockMvc
        .perform(
            get(String.format("/drop_token/%s", game.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(game.getId()));
  }

  @Test
  void shouldNotGetGame() throws Exception {
    mockMvc
        .perform(
            get(String.format("/drop_token/%s", "game123")).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateMove() throws Exception {

    String[][] board = {
      {"", "", "", ""},
      {"", "", "", ""},
      {"", "", "", ""},
      {"", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    PlayerMoveRequest request = new PlayerMoveRequest();
    request.setColumn(0);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.move").isString());
  }

  @Test
  void shouldNotCreateMoveForIllegalMove() throws Exception {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    PlayerMoveRequest request = new PlayerMoveRequest();
    request.setColumn(0);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotCreateMoveForIncorrectPlayerTurn() throws Exception {

    String[][] board = {
      {"", "", "", ""},
      {"", "", "", ""},
      {"", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    PlayerMoveRequest request = new PlayerMoveRequest();
    request.setColumn(0);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player2"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isConflict());

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player2"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotCreateMoveForImaginaryGame() throws Exception {

    PlayerMoveRequest request = new PlayerMoveRequest();
    request.setColumn(0);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", "game1", "player1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldNotCreateMoveForOutOfBoundsMove() throws Exception {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    PlayerMoveRequest request = new PlayerMoveRequest();
    request.setColumn(5);

    String json = MAPPER.writeValueAsString(request);

    mockMvc
        .perform(
            post(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetMove() throws Exception {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    GameEvent moveEvent =
        gameEventRepository.save(
            GameEvent.builder()
                .playerId("player1")
                .gameId(game.getId())
                .type(Event.MOVE)
                .properties(Collections.singletonMap("column", 0))
                .build());

    mockMvc
        .perform(
            get(String.format("/drop_token/%s/moves/%s", game.getId(), moveEvent.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(moveEvent.getId()));
  }

  @Test
  void shouldRemovePlayer() throws Exception {

    String[][] board = {
      {"", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    mockMvc
        .perform(
            delete(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());

    Game winningGame = gameRepository.findById(game.getId()).orElseThrow(NotFoundException::new);

    Assertions.assertEquals(Status.COMPLETE, winningGame.getStatus());
    Assertions.assertEquals("player2", winningGame.getWinner());
  }

  @Test
  void shouldNotRemovePlayerForFinishedGame() throws Exception {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.COMPLETE)
                .winner("player1")
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    mockMvc
        .perform(
            delete(String.format("/drop_token/%s/%s", game.getId(), "player1"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isGone());
  }

  @Test
  void shouldNotRemovePlayerForNonAssociatedPlayer() throws Exception {

    String[][] board = {
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
      {"player1", "", "", ""},
    };

    Game game =
        gameRepository.save(
            Game.builder()
                .players(Arrays.asList("player1", "player2"))
                .status(Status.IN_PROGRESS)
                .board(board)
                .rows(4)
                .columns(4)
                .build());

    mockMvc
        .perform(
            delete(String.format("/drop_token/%s/%s", game.getId(), "player123"))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
