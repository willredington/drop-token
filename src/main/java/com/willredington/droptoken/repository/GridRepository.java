package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GridRepository extends JpaRepository<Grid, Long> {

  Optional<Grid> findOneByGameIdEquals(Long gameId);
}
