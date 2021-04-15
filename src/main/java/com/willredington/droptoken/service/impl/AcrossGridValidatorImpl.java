package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.service.GridValidator;
import com.willredington.droptoken.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class AcrossGridValidatorImpl implements GridValidator {

  @Override
  public Optional<String> findWinner(Game game) {

    String[][] board = game.getBoard();

    for (int rowNum = 0; rowNum < game.getRows(); rowNum++) {

      if (board[rowNum].length > 0) {

        final String firstValue = board[rowNum][0];

        if (!firstValue.equals("")) {

          boolean allMatch = ListUtil.allItemsMatch(Arrays.asList(board[rowNum]));

          if (allMatch) {
            return Optional.of(firstValue);
          }
        }
      }
    }

    return Optional.empty();
  }
}
