package com.sportsbet.fanduel.trading.solution.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sportsbet.fanduel.trading.solution.persistence.entity.serialize.CustomSquadSerializer;
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
@Table(name = "squad", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = CustomSquadSerializer.class)
public class Squad {
  @Id
  @Column(name = "squad_id")
  private Long squadId;

  @Column(name = "squad_name")
  private String squadName;

  @Column(name = "season")
  private int season;

  @Column(name = "team_id")
  private Long teamId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "squad")
  private List<Player> players;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "squad")
  private List<PlayerPosition> playerPositions;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "team_id", insertable = false, updatable = false)
  private Team team;
}
