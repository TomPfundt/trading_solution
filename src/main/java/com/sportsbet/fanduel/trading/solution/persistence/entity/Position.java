package com.sportsbet.fanduel.trading.solution.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "position", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
  @Id
  @Column(name = "position_id")
  private Long positionId;

  @Column(name = "position_name", unique = true)
  private String positionName;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "position")
  private List<PlayerPosition> playerPositions;

  @ManyToOne
  @JoinColumn(name = "sport_id", insertable = false, updatable = false)
  private Sport sport;
}
