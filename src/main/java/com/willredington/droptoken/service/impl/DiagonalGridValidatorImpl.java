package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.service.GridValidator;
import com.willredington.droptoken.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DiagonalGridValidatorImpl implements GridValidator {

  private boolean checkList(List<String> diagonal) {
    return !diagonal.isEmpty() && ListUtil.allItemsMatch(diagonal) && !diagonal.get(0).equals("");
  }

  @Override
  public Optional<String> findWinner(Game game) {

    String[][] board = game.getBoard();
    List<String> diagonal = new ArrayList<>();

    // left to right
    for (int rowNum = 0; rowNum < game.getRows(); rowNum++) {

      for (int colNum = 0; colNum < game.getColumns(); colNum++) {

        if (rowNum == colNum) {
          diagonal.add(board[rowNum][colNum]);
        }
      }
    }

    if (checkList(diagonal)) {
      return Optional.of(diagonal.get(0));
    }

    diagonal = new ArrayList<>();

    // right to left
    for (int rowNum = game.getRows() - 1; rowNum > -1; rowNum--) {

      for (int colNum = 0; colNum > -1; colNum--) {

        if (rowNum == colNum) {
          diagonal.add(board[rowNum][colNum]);
        }
      }
    }

    if (checkList(diagonal)) {
      return Optional.of(diagonal.get(0));
    }

    return Optional.empty();
  }
}
