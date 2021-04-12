package com.willredington.droptoken.service.impl;

import com.willredington.droptoken.dto.PlayerMoveDto;
import com.willredington.droptoken.entity.Grid;
import com.willredington.droptoken.exception.InvalidPlacementException;
import com.willredington.droptoken.repository.GameEventRepository;
import com.willredington.droptoken.repository.GameRepository;
import com.willredington.droptoken.repository.GridPlacementRepository;
import com.willredington.droptoken.repository.GridRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GridServiceImplTest {

  @Mock private GameRepository gameRepository;
  @Mock private GridRepository gridRepository;
  @Mock private GameEventRepository eventRepository;
  @Mock private GridPlacementRepository placementRepository;

  @Test
  void shouldBeInvalidForOutOfBounds() {

    // mocks
    when(gameRepository.existsByIdEquals(anyLong())).thenReturn(true);
    when(gridRepository.findOneByGameIdEquals(anyLong()))
        .thenReturn(Optional.of(Grid.builder().rows(4).columns(4).build()));

    GridServiceImpl service =
        new GridServiceImpl(gameRepository, gridRepository, eventRepository, placementRepository);

    PlayerMoveDto moveDto =
        PlayerMoveDto.builder().gameId(1L).playerId(1L).row(12).column(1222).build();

    Assertions.assertThrows(InvalidPlacementException.class, () -> service.move(moveDto));
  }

  @Test
  void shouldBeInvalidForAlreadyExisting() {

    // mocks
    when(gameRepository.existsByIdEquals(anyLong())).thenReturn(true);
    when(gridRepository.findOneByGameIdEquals(anyLong()))
        .thenReturn(Optional.of(Grid.builder().id(1L).rows(4).columns(4).build()));
    when(placementRepository.existsByGridIdEqualsAndColumnEqualsAndRowEquals(
            anyLong(), anyInt(), anyInt()))
        .thenReturn(true);

    GridServiceImpl service =
        new GridServiceImpl(gameRepository, gridRepository, eventRepository, placementRepository);

    PlayerMoveDto moveDto =
        PlayerMoveDto.builder().gameId(1L).playerId(1L).row(3).column(3).build();

    Assertions.assertThrows(InvalidPlacementException.class, () -> service.move(moveDto));
  }
}
