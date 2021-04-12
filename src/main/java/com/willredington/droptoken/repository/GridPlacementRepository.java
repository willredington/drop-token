package com.willredington.droptoken.repository;

import com.willredington.droptoken.entity.GridPlacement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GridPlacementRepository extends JpaRepository<GridPlacement, Long> {

  List<GridPlacement> findAllByGridIdEquals(Long gridId);

  boolean existsByGridIdEqualsAndColumnEqualsAndRowEquals(Long gridId, int column, int row);
}
