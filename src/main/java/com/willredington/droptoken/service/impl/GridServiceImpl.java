package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.PlayerMoveDto;
import com.willredington.droptoken.entity.GameEvent;
import com.willredington.droptoken.entity.Grid;
import com.willredington.droptoken.entity.GridPlacement;
import com.willredington.droptoken.exception.InvalidPlacementException;
import com.willredington.droptoken.exception.NotFoundException;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.repository.GridPlacementRepository;
import com.willredington.droptoken.repository.GridRepository;
import com.willredington.droptoken.type.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GridServiceImpl {

  private final GameRepository gameRepository;
  private final GridRepository gridRepository;
  private final GameEventRepository eventRepository;
  private final GridPlacementRepository placementRepository;

  public GridServiceImpl(
      GameRepository gameRepository,
      GridRepository gridRepository,
      GameEventRepository eventRepository,
      GridPlacementRepository placementRepository) {
    this.gameRepository = gameRepository;
    this.gridRepository = gridRepository;
    this.eventRepository = eventRepository;
    this.placementRepository = placementRepository;
  }

  private boolean isValidRowColumn(Grid grid, int row, int col) {
    return (row > -1 && row < grid.getRows()) && (col > -1 && col < grid.getColumns());
  }

  public GameEvent move(PlayerMoveDto dto) {

    if (!gameRepository.existsByIdEquals(dto.getGameId())) {
      throw new NotFoundException(String.format("game %d was not found", dto.getGameId()));
    }

    Grid grid =
        gridRepository.findOneByGameIdEquals(dto.getGameId()).orElseThrow(NotFoundException::new);

    if (isValidRowColumn(grid, dto.getRow(), dto.getColumn())) {

      boolean alreadyExists =
          placementRepository.existsByGridIdEqualsAndColumnEqualsAndRowEquals(
              grid.getId(), dto.getColumn(), dto.getRow());

      if (alreadyExists) {
        throw new InvalidPlacementException("placement already exists");
      }

      placementRepository.save(
          GridPlacement.builder()
              .gridId(grid.getId())
              .playerId(dto.getPlayerId())
              .column(dto.getColumn())
              .row(dto.getRow())
              .build());

      return eventRepository.save(
          GameEvent.builder()
              .playerId(dto.getPlayerId())
              .gameId(dto.getGameId())
              .event(Event.MOVE)
              .row(dto.getRow())
              .column(dto.getColumn())
              .build());
    }

    throw new InvalidPlacementException("move is out of bounds");
  }
}
