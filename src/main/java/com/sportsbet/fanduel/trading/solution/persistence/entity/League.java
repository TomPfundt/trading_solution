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
@Table(name = "league", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIncludeProperties({"League Name", "Teams"})
public class League {
  @Id
  @Column(name = "league_id")
  private Long leagueId;

  @JsonProperty("League Name")
  @Column(name = "league_name")
  private String leagueName;

  @Column(name = "sport_id")
  private Long sportId;

  @JsonProperty("Teams")
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "league")
  private List<Team> teams;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "sport_id", insertable = false, updatable = false)
  private Sport sport;
}
