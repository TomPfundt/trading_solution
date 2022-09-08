package com.sportsbet.fanduel.trading.solution.persistence.repository;

import com.sportsbet.fanduel.trading.solution.persistence.entity.PlayerPosition;
import com.sportsbet.fanduel.trading.solution.persistence.entity.id.PlayerPositionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerPositionRepository extends JpaRepository<PlayerPosition, PlayerPositionId> {
  List<PlayerPosition> findAllByPositionIdAndSquadId(Long positionId, Long squadId);
}
