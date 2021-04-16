package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.service.GridValidator;
import com.willredington.droptoken.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    List<String> diagonal1 = new ArrayList<>();
    List<String> diagonal2 = new ArrayList<>();

    for (int rowNum = 0; rowNum < game.getRows(); rowNum++) {

      for (int colNum = 0; colNum < game.getColumns(); colNum++) {

        int oppositeRow = game.getRows() - rowNum - 1;

        String topBottomValue = board[rowNum][colNum];
        String bottomTopValue = board[oppositeRow][colNum];

        if (rowNum == colNum) {
          diagonal1.add(topBottomValue);
          diagonal2.add(bottomTopValue);
        }
      }
    }

    for (List<String> diagonal : Arrays.asList(diagonal1, diagonal2)) {
      if (checkList(diagonal) && diagonal.size() == game.getColumns()) {
        return Optional.of(diagonal.get(0));
      }
    }

    return Optional.empty();
  }
}
