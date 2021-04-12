package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {}
