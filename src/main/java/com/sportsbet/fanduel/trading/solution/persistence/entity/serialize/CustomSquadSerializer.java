package com.sportsbet.fanduel.trading.solution.persistence.entity.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sportsbet.fanduel.trading.solution.persistence.entity.PlayerPosition;
import com.sportsbet.fanduel.trading.solution.persistence.entity.Squad;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CustomSquadSerializer extends JsonSerializer<Squad> {
  public static final Comparator<PlayerPosition> DEPTH_COMPARATOR = Comparator.comparing(PlayerPosition::getDepth);

  @Override
  public void serialize(Squad squad, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

    Map<String, List<DepthChartDto>> positions = squad.getPlayerPositions().stream()
      .sorted(Comparator.comparing(PlayerPosition::getPositionId).thenComparing(DEPTH_COMPARATOR))
      .map(this::mapDepthChartDto)
      .collect(Collectors.groupingBy(DepthChartDto::getPosition, toList()));

    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("Squad Name", squad.getSquadName());
    jsonGenerator.writeObjectFieldStart("Depth Chart");

    for (Map.Entry<String, List<DepthChartDto>> position :  positions.entrySet()) {
      jsonGenerator.writeObjectFieldStart(position.getKey());

      for (DepthChartDto depthChartDto : position.getValue()) {
        jsonGenerator.writeObjectFieldStart(depthChartDto.getDepth().toString());
        jsonGenerator.writeStringField("Name", depthChartDto.getName());
        jsonGenerator.writeNumberField("Number", depthChartDto.getNumber());
        jsonGenerator.writeEndObject();
      }

      jsonGenerator.writeEndObject();
    }

    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndObject();
  }

  private DepthChartDto mapDepthChartDto(PlayerPosition playerPosition) {
    return DepthChartDto.builder()
      .name(playerPosition.getPlayer().getPlayerName())
      .number(playerPosition.getPlayerId())
      .depth(playerPosition.getDepth())
      .position(playerPosition.getPosition().getPositionName())
      .build();
  }

  @Data
  @SuperBuilder
  public static class DepthChartDto {
    private String name;
    private String position;
    private Long number;
    private Integer depth;
  }
}
