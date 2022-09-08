package com.sportsbet.fanduel.trading.solution.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerData {
  private String position;
  private Long playerNumber;
  private int positionDepth;
}
