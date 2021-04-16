package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class DiagonalGridValidatorImplTest {

  @Test
  void shouldNotFindWinner() {
    String[][] board1 = {
      {"X", "X", "", ""},
      {"Y", "Y", "", "Y"},
      {"X", "X", "X", "X"},
      {"", "X", "", "X"},
    };

    Game game = Game.builder().rows(4).columns(4).board(board1).build();
    Assertions.assertEquals(Optional.empty(), new DiagonalGridValidatorImpl().findWinner(game));
  }

  @Test
  void shouldFindWinner() {
    String[][] board1 = {
      {"X", "X", "", ""},
      {"Y", "X", "", "Y"},
      {"X", "X", "X", "X"},
      {"", "X", "", "X"},
    };

    Game game = Game.builder().rows(4).columns(4).board(board1).build();
    Assertions.assertEquals(Optional.of("X"), new DiagonalGridValidatorImpl().findWinner(game));
  }
}
