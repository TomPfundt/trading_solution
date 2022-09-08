package com.sportsbet.fanduel.trading.solution.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "player", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIncludeProperties({"Player Name", "Player Number"})

public class Player {
  @JsonProperty("Player Number")
  @Id
  @Column(name = "player_id")
  private Long playerId;

  @JsonProperty("Player Name")
  @Column(name = "player_name")
  private String playerName;

  @Column(name = "squad_id")
  private Long squadId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
  private List<PlayerPosition> playerPositions;

  @ManyToOne
  @JoinColumn(name = "squad_id", insertable = false, updatable = false)
  private Squad squad;
}
