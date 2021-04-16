package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.GameEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameEventRepository extends MongoRepository<GameEvent, String> {

  List<GameEvent> findAllByGameIdEquals(String gameId);
}
