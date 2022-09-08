package com.sportsbet.fanduel.trading.solution.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "team", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIncludeProperties({"Team Name", "Squads"})
public class Team {
  @Id
  @Column(name = "team_id")
  private Long teamId;

  @JsonProperty("Team Name")
  @Column(name = "team_name")
  private String teamName;

  @Column(name = "league_id")
  private Long leagueId;

  @JsonProperty("Squads")
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
  private List<Squad> squads;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "league_id", insertable = false, updatable = false)
  private League league;
}
