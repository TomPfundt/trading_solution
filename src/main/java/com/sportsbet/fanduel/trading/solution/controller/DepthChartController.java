package com.sportsbet.fanduel.trading.solution.controller;

import com.sportsbet.fanduel.trading.solution.controller.model.PlayerData;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Player;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Sport;
import com.sportsbet.fanduel.trading.solution.service.DepthChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/depth-chart", produces = APPLICATION_JSON_VALUE)
public class DepthChartController {
  private final DepthChartService depthChartService;

  @PostMapping(value = "/player/add", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addPlayerToDepthChart(@RequestBody PlayerData body) {
    boolean success = depthChartService.addPlayerPosition(body.getPosition(), body.getPlayerNumber(), body.getPositionDepth());
    return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "/player/delete", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Player> removePlayerFromDepthChart(@RequestBody PlayerData body) {
    Player player = depthChartService.removePlayerPosition(body.getPosition(), body.getPlayerNumber());
    return player != null ? ResponseEntity.ok(player) : ResponseEntity.badRequest().build();

  }

  @GetMapping("/{position}/player/{playerNumber}/backups")
  public ResponseEntity<List<Player>> getBackups(
    @PathVariable String position,
    @PathVariable Long playerNumber
  ) {
    List<Player> backups = depthChartService.getBackups(position, playerNumber);
    return ResponseEntity.ok(backups);
  }

  @GetMapping("")
  public ResponseEntity<List<Sport>> getFullDepthChart() {
    List<Sport> fullDepthChart = depthChartService.getFullDepthChart();
    return ResponseEntity.ok(fullDepthChart);
  }
}
