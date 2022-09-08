package com.sportsbet.fanduel.trading.solution.persistence.entity;

import com.sportsbet.fanduel.trading.solution.persistence.entity.id.PlayerPositionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(PlayerPositionId.class)
@Table(name = "player_position", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPosition {
  @Id
  @Column(name = "player_id")
  private Long playerId;

  @Id
  @Column(name = "position_id")
  private Long positionId;

  @Column(name = "squad_id")
  private Long squadId;

  @Column(name = "depth")
  private int depth;

  @ManyToOne
  @JoinColumn(name = "player_id", insertable = false, updatable = false)
  private Player player;

  @ManyToOne
  @JoinColumn(name = "position_id", insertable = false, updatable = false)
  private Position position;

  @ManyToOne
  @JoinColumn(name = "squad_id", insertable = false, updatable = false)
  private Squad squad;
}
