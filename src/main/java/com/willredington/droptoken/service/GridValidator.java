package com.willredington.droptoken.service;

import com.willredington.droptoken.entity.Game;

import java.util.Optional;

public interface GridValidator {

  Optional<String> findWinner(Game game);
}
