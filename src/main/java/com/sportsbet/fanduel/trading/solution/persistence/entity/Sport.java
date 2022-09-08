package com.sportsbet.fanduel.trading.solution.persistence.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "sport", schema = "TRADING_SOLUTION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIncludeProperties({"Sport Name", "Leagues"})
public class Sport {
  @Id
  @Column(name = "sport_id")
  private Long sportId;

  @JsonProperty("Sport Name")
  @Column(name = "sport_name")
  private String sportName;

  @JsonProperty("Leagues")
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sport")
  private List<League> leagues;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sport")
  private List<Position> positions;
}
