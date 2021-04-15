package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AcrossGridValidatorImplTest {

  @Test
  void shouldNotFindWinner() {

    String[][] board = {
      {"", "", "", ""},
      {"", "", "", ""},
      {"", "", "", ""},
      {"", "", "", ""},
    };

    Game game = Game.builder().rows(4).columns(4).board(board).build();

    Assertions.assertEquals(Optional.empty(), new AcrossGridValidatorImpl().findWinner(game));
  }

  @Test
  void shouldFindWinner() {

    String[][] board = {
      {"", "", "", ""},
      {"Y", "X", "Y", "Y"},
      {"X", "X", "X", "X"},
      {"", "", "", ""},
    };

    Game game = Game.builder().rows(4).columns(4).board(board).build();

    Assertions.assertEquals(Optional.of("X"), new AcrossGridValidatorImpl().findWinner(game));
  }
}
