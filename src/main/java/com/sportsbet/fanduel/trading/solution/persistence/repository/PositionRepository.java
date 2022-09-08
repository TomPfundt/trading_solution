package com.sportsbet.fanduel.trading.solution.persistence.repository;

import com.sportsbet.fanduel.trading.solution.persistence.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
  Position findByPositionName(String positionName);
}
