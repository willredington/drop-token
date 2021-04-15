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
public class VerticalGridValidatorImpl implements GridValidator {

  @Override
  public Optional<String> findWinner(Game game) {

    String[][] board = game.getBoard();

    for (int colNum = 0; colNum < game.getColumns(); colNum++) {

      List<String> row = new ArrayList<>();

      for (int rowNum = 0; rowNum < game.getRows(); rowNum++) {
        row.add(board[rowNum][colNum]);
      }

      if (ListUtil.allItemsMatch(row) && !row.get(0).equals("")) {
        return Optional.of(row.get(0));
      }
    }

    return Optional.empty();
  }
}
