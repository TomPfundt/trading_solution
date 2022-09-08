package com.sportsbet.fanduel.trading.solution.service;

import com.sportsbet.fanduel.trading.solution.persistence.entity.Player;
import com.sportsbet.fanduel.trading.solution.persistence.entity.PlayerPosition;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Position;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Sport;
import com.sportsbet.fanduel.trading.solution.persistence.entity.id.PlayerPositionId;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PlayerPositionRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PlayerRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.PositionRepository;
import com.sportsbet.fanduel.trading.solution.persistence.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepthChartService {
  public static final Comparator<PlayerPosition> DEPTH_COMPARATOR = Comparator.comparing(PlayerPosition::getDepth);
  private final PlayerRepository playerRepository;
  private final PositionRepository positionRepository;
  private final PlayerPositionRepository playerPositionRepository;
  private final SportRepository sportRepository;

  public boolean addPlayerPosition(String position, Long playerId, Integer positionDepth) {
    log.info("DepthChartService::addPlayerPosition fetch player and position details.");
    Player player = playerRepository.findById(playerId).orElse(null);
    Position positionEntity = positionRepository.findByPositionName(position);

    if (player == null || positionEntity == null) {
      log.info("DepthChartService::addPlayerPosition player or position details not found.");
      return false;
    }

    log.info("DepthChartService::addPlayerPosition build a new player position entity.");
    PlayerPosition playerPosition = PlayerPosition.builder().playerId(playerId).positionId(positionEntity.getPositionId()).squadId(player.getSquadId()).build();

    log.info("DepthChartService::addPlayerPosition fetch existing player position entities for squad id={} and position id={}.", player.getSquadId(), positionEntity.getPositionId());
    List<PlayerPosition> squadPositions = playerPositionRepository.findAllByPositionIdAndSquadId(positionEntity.getPositionId(), player.getSquadId());

    updatePlayerPositions(positionDepth, squadPositions, playerPosition);

    log.info("DepthChartService::addPlayerPosition save updated player position entities for squad id={} and position id={}.", player.getSquadId(), positionEntity.getPositionId());
    playerPositionRepository.saveAll(squadPositions);
    return true;
  }

  private void updatePlayerPositions(Integer positionDepth, List<PlayerPosition> squadPositions, PlayerPosition playerPosition) {
    log.info("DepthChartService::updatePlayerPositions check if player id={} already exists in depth chart.", playerPosition.getPlayerId());
    PlayerPosition existingPlayer = squadPositions.stream().filter(position -> position.getPlayerId().equals(playerPosition.getPlayerId())).findFirst().orElse(null);

    if (existingPlayer != null) {
      log.info("DepthChartService::updatePlayerPositions remove existing player id={} from depth chart.", playerPosition.getPlayerId());
      squadPositions.remove(existingPlayer);

      log.info("DepthChartService::updatePlayerPositions increment backup depths in depth chart.");
      squadPositions.stream()
        .filter(position -> position.getDepth() > existingPlayer.getDepth())
        .forEach(chart -> chart.setDepth(chart.getDepth() - 1));
    }

    int maxDepth = getMaxDepth(squadPositions);
    if (positionDepth == null || positionDepth > maxDepth) {
      log.info("DepthChartService::updatePlayerPositions no depth provided, or provided depth is greater than max depth - adding player to max depth.");
      playerPosition.setDepth(maxDepth + 1);
    } else {
      log.info("DepthChartService::updatePlayerPositions update depth chart values in preparation for new player insertion.");
      squadPositions.stream()
        .filter(position -> position.getDepth() >= positionDepth)
        .forEach(chart -> chart.setDepth(chart.getDepth() + 1));

      playerPosition.setDepth(positionDepth);
    }

    log.info("DepthChartService::updatePlayerPositions insert player id={} into depth chart.", playerPosition.getPlayerId());
    squadPositions.add(playerPosition);
  }

  private int getMaxDepth(List<PlayerPosition> squadPositions) {
    return squadPositions.stream()
      .mapToInt(PlayerPosition::getDepth)
      .max()
      .orElse(-1);
  }

  public Player removePlayerPosition(String position, Long playerId) {
    log.info("DepthChartService::removePlayerPosition fetch player and position details for player id={} and position={}.", playerId, position);
    Position positionEntity = positionRepository.findByPositionName(position);

    PlayerPositionId id = getId(playerId, positionEntity.getPositionId());
    PlayerPosition playerPosition = playerPositionRepository.findById(id).orElse(null);

    if (playerPosition == null) {
      log.warn("DepthChartService::removePlayerPosition no details found for player id={} and position={}.", playerId, position);
      return null;
    }

    log.info("DepthChartService::removePlayerPosition delete player position details for player id={} and position={}.", playerId, position);
    playerPositionRepository.delete(playerPosition);

    log.info("DepthChartService::removePlayerPosition find all remaining squad player position details for position={}.", position);
    List<PlayerPosition> squadPositions = playerPositionRepository.findAllByPositionIdAndSquadId(positionEntity.getPositionId(), playerPosition.getSquadId());

    log.info("DepthChartService::removePlayerPosition update depth chart values to fill gap of removed player for position={}.", position);
    squadPositions.stream()
      .filter(playerPos -> playerPos.getDepth() > playerPosition.getDepth())
      .forEach(playerPos -> playerPos.setDepth(playerPos.getDepth() - 1));

    log.info("DepthChartService::removePlayerPosition save updated player position entities for squad id={} and position={}.", playerPosition.getSquadId(), position);
    playerPositionRepository.saveAll(squadPositions);

    return playerPosition.getPlayer();
  }

  private PlayerPositionId getId(Long playerId, Long positionId) {
    return PlayerPositionId.builder()
      .playerId(playerId)
      .positionId(positionId)
      .build();
  }

  public List<Player> getBackups(String position, Long playerId) {
    log.info("DepthChartService::getBackups fetch player and position details.");
    Position positionEntity = positionRepository.findByPositionName(position);
    Player player = playerRepository.findById(playerId).orElse(null);

    if (player == null || positionEntity == null) {
      log.warn("DepthChartService::getBackups player or position details not found.");
      return List.of();
    }

    log.info("DepthChartService::getBackups get all player position details for squad id={}.", player.getSquadId());
    List<PlayerPosition> squadPositions = playerPositionRepository.findAllByPositionIdAndSquadId(positionEntity.getPositionId(), player.getSquadId());

    log.info("DepthChartService::getBackups filter player position details for player id={}.", player.getPlayerId());
    PlayerPosition playerPosition = squadPositions.stream()
      .filter(playerPos -> playerPos.getPlayerId().equals(playerId))
      .findFirst()
      .orElse(null);

    if (playerPosition == null) {
      log.warn("DepthChartService::getBackups player not found for position={}.", position);
      return List.of();
    }

    log.info("DepthChartService::getBackups filter backups, sort and return backup player detail.");
    return squadPositions.stream()
      .filter(playerPos -> playerPos.getDepth() > playerPosition.getDepth())
      .sorted(DEPTH_COMPARATOR)
      .map(PlayerPosition::getPlayer)
      .collect(Collectors.toList());
  }

  public List<Sport> getFullDepthChart() {
    log.info("DepthChartService::getFullDepthChart return full depth chart.");
    return sportRepository.findAll();
  }



}
