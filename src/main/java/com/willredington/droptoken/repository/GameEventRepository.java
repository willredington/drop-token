package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameEventRepository extends JpaRepository<GameEvent, Long> {}
