/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.factory;

import com.tdudhatra.games.connect4.dto.GameMoveHistoryDto;
import com.tdudhatra.games.connect4.entity.GameMoveHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component class for building DTOs/Entities from Entities/DTOs.
 * 
 * @author tdudhatra
 */

@Component
public class GameMoveHistoryObjectFactory
    implements ObjectFactory<GameMoveHistoryDto, GameMoveHistory> {

  @Autowired
  GameObjectFactory gameObjectFactory;

  @Override
  public GameMoveHistoryDto buildDtoFromEntity(GameMoveHistory gameMoveHist) {
    if (gameMoveHist == null) {
      return null;
    }

    GameMoveHistoryDto gameMoveHistDto = new GameMoveHistoryDto(gameMoveHist.getId(),
        gameMoveHist.getGuid(), gameMoveHist.getCreateTime(), gameMoveHist.getUpdateTime(),
        gameObjectFactory.buildDtoFromEntity(gameMoveHist.getGame()), gameMoveHist.getMovesJson());

    return gameMoveHistDto;
  }

  @Override
  public GameMoveHistory buildEntityFromDto(GameMoveHistoryDto gameMoveHistDto) {
    if (gameMoveHistDto == null) {
      return null;
    }

    GameMoveHistory gameMoveHist = new GameMoveHistory();
    gameMoveHist.setId(gameMoveHistDto.getId());
    gameMoveHist.setGuid(gameMoveHistDto.getGuid());
    gameMoveHist.setCreateTime(gameMoveHistDto.getCreateTime());
    gameMoveHist.setGame(gameObjectFactory.buildEntityFromDto(gameMoveHistDto.getGame()));
    gameMoveHist.setMovesJson(gameMoveHistDto.getMovesJson());

    return gameMoveHist;
  }

}
