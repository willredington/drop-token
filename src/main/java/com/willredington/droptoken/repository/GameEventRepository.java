package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.GameEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameEventRepository extends MongoRepository<GameEvent, String> {}
