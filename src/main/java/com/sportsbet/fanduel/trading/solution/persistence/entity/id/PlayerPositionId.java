package com.sportsbet.fanduel.trading.solution.persistence.entity.id;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
public class PlayerPositionId implements Serializable {
  private Long playerId;
  private Long positionId;
}
