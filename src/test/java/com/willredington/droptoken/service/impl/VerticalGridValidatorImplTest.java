package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class VerticalGridValidatorImplTest {

  @Test
  void shouldNotFindWinner() {

    String[][] board = {
      {"X", "X", "", ""},
      {"Y", "Y", "", "Y"},
      {"X", "X", "", "X"},
      {"", "X", "", ""},
    };

    Game game = Game.builder().rows(4).columns(4).board(board).build();
    Assertions.assertEquals(Optional.empty(), new VerticalGridValidatorImpl().findWinner(game));
  }

  @Test
  void shouldFindWinner() {

    String[][] board = {
      {"X", "X", "", ""},
      {"Y", "X", "Y", "Y"},
      {"X", "X", "X", "X"},
      {"", "X", "", ""},
    };

    Game game = Game.builder().rows(4).columns(4).board(board).build();
    Assertions.assertEquals(Optional.of("X"), new VerticalGridValidatorImpl().findWinner(game));
  }
}
