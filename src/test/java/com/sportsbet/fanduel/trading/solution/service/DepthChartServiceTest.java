package com.sportsbet.fanduel.trading.solution.service;

import com.sportsbet.fanduel.trading.solution.persistence.entity.Player;
import com.sportsbet.fanduel.trading.solution.persistence.entity.PlayerPosition;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Position;
import com.sportsbet.fanduel.trading.solution.persistence.entity.id.PlayerPositionId;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PlayerPositionRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PlayerRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PositionRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.SportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepthChartServiceTest {
  public static final String POSITION_NAME = "Position Name";
  @Mock
  private PlayerRepository playerRepository;
  @Mock
  private PositionRepository positionRepository;
  @Mock
  private PlayerPositionRepository playerPositionRepository;
  @Mock
  private SportRepository sportRepository;
  @InjectMocks
  private DepthChartService depthChartService;

  @Captor
  private ArgumentCaptor<List<PlayerPosition>> playerPositionCaptor;

  @Test
  void addPlayerPosition_playerNotFound() {
    long playerId = new Random().nextLong();
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 0);

    assertFalse(success);
  }

  @Test
  void addPlayerPosition_positionNotFound() {
    long playerId = new Random().nextLong();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().build()));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(null);
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 0);
    assertFalse(success);
  }

  @Test
  void addPlayerPosition_emptyPlayerPositions_add() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 0);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertEquals(1, playerPositions.size());
    assertEquals(playerId, playerPositions.get(0).getPlayerId());
    assertEquals(positionId, playerPositions.get(0).getPositionId());
    assertEquals(0, playerPositions.get(0).getDepth());
    assertTrue(success);
  }

  @Test
  void addPlayerPosition_emptyPlayerPositions_add_noDepth() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, null);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertEquals(1, playerPositions.size());
    assertEquals(playerId, playerPositions.get(0).getPlayerId());
    assertEquals(positionId, playerPositions.get(0).getPositionId());
    assertEquals(0, playerPositions.get(0).getDepth());
    assertTrue(success);
  }

  @Test
  void addPlayerPosition_emptyPlayerPositions_add_depthGreaterThan0() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertEquals(1, playerPositions.size());
    assertEquals(playerId, playerPositions.get(0).getPlayerId());
    assertEquals(positionId, playerPositions.get(0).getPositionId());
    assertEquals(0, playerPositions.get(0).getDepth());
    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_addToStart() {
    long playerId = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(1).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 0);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> playerId == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == playerId).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_addToMiddle() {
    long playerId = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(1).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> playerId == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == playerId).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_addToEnd() {
    long playerId = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(1).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 2);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> playerId == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == playerId).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateFromEndToStart() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(player1Id).squadId(squadId).build();
    when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(2).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, player1Id, 0);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateFromMiddleToStart() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(player1Id).squadId(squadId).build();
    when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(2).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, player1Id, 0);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateFromStartToMiddle() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(player1Id).squadId(squadId).build();
    when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(2).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, player1Id, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateFromStartToEnd() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(player1Id).squadId(squadId).build();
    when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(2).build()
    );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, player1Id, 2);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }
  @Test
  void addPlayerPosition_existingPlayerPositions_updateFromEndToMiddle() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(player1Id).squadId(squadId).build();
    when(playerRepository.findById(player1Id)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    List<PlayerPosition> playerPositions = List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(2).build()
      );
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>(playerPositions));
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, player1Id, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositionsValue = playerPositionCaptor.getValue();
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(1, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositionsValue.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(2, playerPositionsValue.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());

    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateToMiddleFromEnd() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertEquals(1, playerPositions.size());
    assertEquals(playerId, playerPositions.get(0).getPlayerId());
    assertEquals(positionId, playerPositions.get(0).getPositionId());
    assertEquals(0, playerPositions.get(0).getDepth());
    assertTrue(success);
  }

  @Test
  void addPlayerPosition_existingPlayerPositions_updateToEnd() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    Player player = Player.builder().playerId(playerId).squadId(squadId).build();
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).positionName(POSITION_NAME).build());
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(new ArrayList<>());
    boolean success = depthChartService.addPlayerPosition(POSITION_NAME, playerId, 1);

    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertEquals(1, playerPositions.size());
    assertEquals(playerId, playerPositions.get(0).getPlayerId());
    assertEquals(positionId, playerPositions.get(0).getPositionId());
    assertEquals(0, playerPositions.get(0).getDepth());
    assertTrue(success);
  }
  //    PlayerPosition playerPosition = PlayerPosition.builder().playerId(playerId).positionId(positionEntity.getPositionId()).squadId(player.getSquadId()).build();
  //
  //    List<PlayerPosition> squadPositions = playerPositionRepository.findAllByPositionIdAndSquadId(positionEntity.getPositionId(), player.getSquadId());
  //
  //    updatePlayerPositions(positionDepth, squadPositions, playerPosition);
  //
  //    playerPositionRepository.saveAll(squadPositions);
  //    return true;
  //  }
  //
  //  private void updatePlayerPositions(Integer positionDepth, List<PlayerPosition> squadPositions, PlayerPosition playerPosition) {
  //    PlayerPosition existingPlayer = squadPositions.stream().filter(position -> position.getPlayerId().equals(playerPosition.getPlayerId())).findFirst().orElse(null);
  //
  //    if (existingPlayer != null) {
  //      squadPositions.remove(existingPlayer);
  //
  //      squadPositions.stream()
  //        .filter(position -> position.getDepth() > existingPlayer.getDepth())
  //        .forEach(chart -> chart.setDepth(chart.getDepth() - 1));
  //    }
  //
  //    int maxDepth = getMaxDepth(squadPositions);
  //    if (positionDepth == null || positionDepth > maxDepth) {
  //      playerPosition.setDepth(maxDepth + 1);
  //    } else {
  //      squadPositions.stream()
  //        .filter(position -> position.getDepth() >= positionDepth)
  //        .forEach(chart -> chart.setDepth(chart.getDepth() + 1));
  //
  //      playerPosition.setDepth(positionDepth);
  //    }
  //
  //    log.info("DepthChartService::updatePlayerPositions insert player id={} into depth chart.", playerPosition.getPlayerId());
  //    squadPositions.add(playerPosition);
  //  }

  @Test
  void removePlayerPosition_notFound() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    when(playerPositionRepository.findById(isA(PlayerPositionId.class))).thenReturn(Optional.empty());

    Player player = depthChartService.removePlayerPosition(POSITION_NAME, playerId);
    assertNull(player);
  }

  @Test
  void removePlayerPosition_singleDepth() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    PlayerPosition playerPosition = PlayerPosition.builder().playerId(playerId).squadId(squadId).positionId(positionId).depth(0).player(Player.builder().playerId(playerId).build()).build();
    when(playerPositionRepository.findById(isA(PlayerPositionId.class))).thenReturn(Optional.of(playerPosition));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(playerPosition));

    Player player = depthChartService.removePlayerPosition(POSITION_NAME, playerId);
    verify(playerPositionRepository, times(1)).delete(playerPosition);
    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    assertEquals(playerId, player.getPlayerId());
  }

  @Test
  void removePlayerPosition_startMultipleDepth() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    PlayerPosition playerPosition1 = PlayerPosition.builder().playerId(player1Id).squadId(squadId).positionId(positionId).depth(0).player(Player.builder().playerId(player1Id).build()).build();

    when(playerPositionRepository.findById(isA(PlayerPositionId.class))).thenReturn(Optional.of(playerPosition1));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(
      playerPosition1,
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(2).build()
    ));

    Player player = depthChartService.removePlayerPosition(POSITION_NAME, player1Id);
    verify(playerPositionRepository, times(1)).delete(playerPosition1);
    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    assertEquals(player1Id, player.getPlayerId());

    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(0, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(1, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());
  }

  @Test
  void removePlayerPosition_middleMultipleDepth() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    PlayerPosition playerPosition2 = PlayerPosition.builder().playerId(player2Id).squadId(squadId).positionId(positionId).depth(1).player(Player.builder().playerId(player2Id).build()).build();

    when(playerPositionRepository.findById(isA(PlayerPositionId.class))).thenReturn(Optional.of(playerPosition2));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(0).build(),
      playerPosition2,
      PlayerPosition.builder().squadId(squadId).playerId(player3Id).positionId(positionId).depth(2).build()
    ));

    Player player = depthChartService.removePlayerPosition(POSITION_NAME, player2Id);
    verify(playerPositionRepository, times(1)).delete(playerPosition2);
    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    assertEquals(player2Id, player.getPlayerId());

    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(0, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player3Id == id));
    assertEquals(1, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player3Id).findFirst().get().getDepth());
  }

  @Test
  void removePlayerPosition_endMultipleDepth() {
    long player1Id = new Random().nextLong();
    long player2Id = new Random().nextLong();
    long player3Id = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    PlayerPosition playerPosition3 = PlayerPosition.builder().playerId(player3Id).squadId(squadId).positionId(positionId).depth(2).player(Player.builder().playerId(player3Id).build()).build();

    when(playerPositionRepository.findById(isA(PlayerPositionId.class))).thenReturn(Optional.of(playerPosition3));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(
      PlayerPosition.builder().squadId(squadId).playerId(player1Id).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(player2Id).positionId(positionId).depth(1).build(),
      playerPosition3
    ));

    Player player = depthChartService.removePlayerPosition(POSITION_NAME, player3Id);
    verify(playerPositionRepository, times(1)).delete(playerPosition3);
    verify(playerPositionRepository, times(1)).saveAll(playerPositionCaptor.capture());
    assertEquals(player3Id, player.getPlayerId());

    List<PlayerPosition> playerPositions = playerPositionCaptor.getValue();
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player1Id == id));
    assertEquals(0, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player1Id).findFirst().get().getDepth());
    assertTrue(playerPositions.stream().map(PlayerPosition::getPlayerId).anyMatch(id -> player2Id == id));
    assertEquals(1, playerPositions.stream().filter(playerPos -> playerPos.getPlayerId() == player2Id).findFirst().get().getDepth());

  }

  @Test
  void getBackups_success_depth0_with2Backups() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    long backupPlayer1Id = new Random().nextLong();
    long backupPlayer2Id = new Random().nextLong();

    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().squadId(squadId).build()));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(
      PlayerPosition.builder().squadId(squadId).playerId(playerId).positionId(positionId).depth(0).build(),
      PlayerPosition.builder().squadId(squadId).playerId(backupPlayer1Id).positionId(positionId).depth(1).player(Player.builder().playerId(backupPlayer1Id).build()).build(),
      PlayerPosition.builder().squadId(squadId).playerId(backupPlayer2Id).positionId(positionId).depth(2).player(Player.builder().playerId(backupPlayer2Id).build()).build()
    ));
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertEquals(2, backups.size());
    assertTrue(backups.stream().map(Player::getPlayerId).anyMatch(id -> backupPlayer1Id == id));
    assertTrue(backups.stream().map(Player::getPlayerId).anyMatch(id -> backupPlayer2Id == id));
  }

  @Test
  void getBackups_success_depth1_with1HigherPriorityAnd1Backup() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();
    long backupPlayer1Id = new Random().nextLong();
    long backupPlayer2Id = new Random().nextLong();

    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().squadId(squadId).build()));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(
      PlayerPosition.builder().squadId(squadId).playerId(playerId).positionId(positionId).depth(1).build(),
      PlayerPosition.builder().squadId(squadId).playerId(backupPlayer1Id).positionId(positionId).depth(2).player(Player.builder().playerId(backupPlayer1Id).build()).build(),
      PlayerPosition.builder().squadId(squadId).playerId(backupPlayer2Id).positionId(positionId).depth(0).player(Player.builder().playerId(backupPlayer2Id).build()).build()
    ));
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertEquals(1, backups.size());
    assertTrue(backups.stream().map(Player::getPlayerId).anyMatch(id -> backupPlayer1Id == id));
    assertTrue(backups.stream().map(Player::getPlayerId).noneMatch(id -> backupPlayer2Id == id));
  }

  @Test
  void getBackups_noBackups() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();

    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().squadId(squadId).build()));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of(PlayerPosition.builder().squadId(squadId).playerId(playerId).positionId(positionId).depth(0).build()));
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertTrue(backups.isEmpty());
  }

  @Test
  void getBackups_nullPlayer() {
    long playerId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().build());
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertTrue(backups.isEmpty());
  }

  @Test
  void getBackups_nullPosition() {
    long playerId = new Random().nextLong();
    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(null);
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().build()));
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertTrue(backups.isEmpty());
  }

  @Test
  void getBackups_nullPlayerPosition() {
    long playerId = new Random().nextLong();
    long positionId = new Random().nextLong();
    long squadId = new Random().nextLong();

    when(positionRepository.findByPositionName(POSITION_NAME)).thenReturn(Position.builder().positionId(positionId).build());
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(Player.builder().squadId(squadId).build()));
    when(playerPositionRepository.findAllByPositionIdAndSquadId(positionId, squadId)).thenReturn(List.of());
    List<Player> backups = depthChartService.getBackups(POSITION_NAME, playerId);
    assertTrue(backups.isEmpty());
  }

  @Test
  void getFullDepthChart() {
    depthChartService.getFullDepthChart();
    verify(sportRepository, times(1)).findAll();
  }
}