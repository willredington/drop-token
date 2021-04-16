package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.Game;
import com.willredington.droptoken.type.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {

  boolean existsByIdEquals(String gameId);

  List<Game> findAllByStatusEquals(Status status);
}
